package codigo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import codigo.EstadoFinal;
import codigo.RegraTransicao;

public class AFD {
	String nomeSintaxe;
	String arquivoConfig;
	String tabSimbolos;
	String resultado;
	String[] tipoTokens;
	boolean frecon=false;
	HashMap<String, String> sintaxes;
	int input;//ID do token onde o AFD começa o reconhecimento.
	boolean output;//RECONHECE OU NAO A SINTAXE
	String mensagem="";//Mensagem de erro (se houver)
	/*ATRIBUTOS USADOS PARA PERCORRER O AFD*/
	String estadoInicial;
	String estadoAtual;
	LinkedList<String> estados = new LinkedList<String>();
	LinkedList<EstadoFinal> estadosFinais = new LinkedList<EstadoFinal>();
	LinkedList<RegraTransicao> regrastransicao = new LinkedList<RegraTransicao>();
	LinkedList<String> termosValidos;
	
	/**
	 * Construtor da classe
	 * @param nome - obrigatorio [nome da sintaxe]
	 * @param nomeArq - arquivo de configuracao
	 * @param nomeTabSimbolos - arquivo tab simbolos
	 */
	public AFD(String nome,String arqConfig, 
			String nomeTabSimbolos) {
		nomeSintaxe=nome;
		arquivoConfig=arqConfig;
		tabSimbolos=nomeTabSimbolos;
		//le_config(arquivoConfig);
	}
	
	/**
	 * Construtor da classe - versao debug 
	 * com lista de tipos de tokens
	 * @param nome
	 * @param arqConfig
	 * @param tks
	 */
	public AFD(String nome, String arqConfig, 
			String[] tks, LinkedList termos) {
		nomeSintaxe=nome;
		arquivoConfig=arqConfig;
		tipoTokens = tks;
		termosValidos = termos;
	}
	
	public void setSintaxes(HashMap<String,String> s) {
		sintaxes = s;
	}
	
	public int executa(int id_inicio) {
		this.le_config();
		output=false;
		String resposta="";
		for(input = id_inicio;input<tipoTokens.length;input++) {
			resposta = percorreAFD(tipoTokens[input]);
			if(resposta.startsWith("NAO RECONHECIDO")){
				System.out.println(resposta);
				break;
			}
			if(output==true) {
				System.out.println(buscaEFinais(estadoAtual));
				return input;
			}
		}
		return id_inicio;
	}

	public String percorreAFD(String termo){
		String resposta = "NAO RECONHECIDO";
		for(int i=0;i<regrastransicao.size();i++) {
			RegraTransicao regra = regrastransicao.get(i);
			if(regra.estadoinicial.equals(estadoAtual) 
					&& regra.simbolos.startsWith("SINT")
					&& this.frecon==false){
				System.out.println("O Simbolo eh uma chave de sintaxe!");
				System.out.println("Abrindo AFD: "+regra.simbolos);
				AFD afd = new AFD(regra.simbolos,sintaxes.get(regra.simbolos), tipoTokens,termosValidos);
				afd.setSintaxes(sintaxes);
				input = afd.executa(input);
				termo = afd.resultado;
				frecon=afd.output;
				System.out.println("Retornando ao: "+nomeSintaxe);
				//i--;
			}
			System.out.println(nomeSintaxe
					+":"+estadoAtual+":"+termo+":"
					+regra.estadoinicial+" "+regra.simbolos);
			if(regra.estadoinicial.equals(estadoAtual) 
					&& regra.simbolos.contains(termo)) {
				System.out.println(nomeSintaxe+":"+this.estadoAtual+":"+termo+":"+regra.estadofinal);
				this.estadoAtual=regra.estadofinal;
				this.frecon=false;
				resposta = buscaEFinais(this.estadoAtual);
				if(!resposta.isEmpty()) {
					output=true;//reconheceu os termos
				}				
				break;
			}
		}
		resultado = termo;
		
		if (resposta.contains("NAO RECONHECIDO")) {
			System.out.println("Nao ha regra de transicao adequada de " + estadoAtual + 
					" para o termo " + termo + " com a sintaxe " + nomeSintaxe);
			System.exit(1);
		}
		
		return resposta;
	}	
	
	private String buscaEFinais(String estado) {
		String mensagem="";
		for(int j=0;j<estadosFinais.size();j++) {
			EstadoFinal ef = estadosFinais.get(j);
			//System.out.println(ef.nomeestado+"contains"+estadoAtual);
			if(ef.nomeestado.equals(estado)) {
				mensagem = "RECONHECIDO: "+ef.tipo;
				resultado = ef.tipo;
				break;
			}
		}
		return mensagem;
	}
	
	private void le_config() {
		String arquivoConfig = this.arquivoConfig;
		try {
			BufferedReader br = new BufferedReader(new FileReader(arquivoConfig));
			String linha = br.readLine();
			int index=0;
			while(linha!=null) {//percorre as linhas
				linha = linha.trim();
				if(index==0) {//primeira linha
					//estou lendo a linha dos nomes dos estados
					String nomesE[] = linha.split(",");
					for(int i=0;i<nomesE.length;i++)
						estados.add(nomesE[i]);//adiciono os nomes no linkedList estados				
				}
				if(index==1) {//segunda linha
					//estou lendo o estado inicial
					estadoInicial = linha;
					estadoAtual = estadoInicial;
				}
				if(index==2) {//terceira linha
					//estados finais
					String ef[] = linha.split(",");
					for(int i=0;i<ef.length;i++) {
						//System.out.println(ef[i]);
						String efinal[] = ef[i].split(":");
						EstadoFinal estadoFinal = 
								new EstadoFinal(efinal[0],efinal[1]);
						estadosFinais.add(estadoFinal);
					}
						
				}
				if(index>=3) {//quarta linha em diante
					//lendo as regras de transicao
					String rt[] = linha.split(":");
					RegraTransicao regra = 
							new RegraTransicao(rt[0],rt[1],rt[2]);
					regrastransicao.add(regra);
				}
				linha = br.readLine();
				index++;
			}
			br.close();//fecha o arquivo de conf.
		} catch (FileNotFoundException e) {
			System.out.println("Arquivo nao encontrado.");
		} catch (IOException e) {
			System.out.println("Nao foi possivel abrir o arquivo.");
		}
	}

	public void escreve_mensagem() {}

}