package source;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Classe do analisador sintatico
 * @author mirko
 *
 */
public class Asin {
	/*reaproveitando o codigo para o AFD*/
	String estadoInicial,eInicial;
	String estadoAtual;
	LinkedList<String> estados = new LinkedList<String>();
	LinkedList<EstadoFinal> estadosFinais = new LinkedList<EstadoFinal>();
	LinkedList<RegraTransicao> regrastransicao = new LinkedList<RegraTransicao>();
	LinkedList<Token> tabelaSimbolos = new LinkedList<Token>();

	public void le_configAFD(String nomeArquivo) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
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
					eInicial = estadoInicial;
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
	
	private String buscaEFinais(String estado) {
		String mensagem="";
		for(int j=0;j<estadosFinais.size();j++) {
			EstadoFinal ef = estadosFinais.get(j);
			//System.out.println(ef.nomeestado+"contains"+estadoAtual);
			if(ef.nomeestado.equals(estado)) {
				mensagem = ef.tipo;
				break;
			}
		}
		return mensagem;
	}
	
	/**
	 * Metodo que le a tabela de simbolos
	 * @param nomeArq - nome do arquivo contendo a tabela
	 */
	public void le_tabSimbolos(String nomeArq) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(nomeArq));
			int idx=0;
			String linha = br.readLine();
			while(linha!=null) {
				if(idx!=0) {
					String colunas[]=linha.split(",");
					System.out.println(colunas[4]);
					String eretorno = percorreAFD(colunas[4]);
					String ef = buscaEFinais(eretorno);
					if(!ef.isEmpty()) {
						System.out.println(ef);
						estadoInicial = eInicial;
						
					}
				}				
				linha = br.readLine();
				idx++;
			}
		} catch (FileNotFoundException e) {
			System.out.println("Nao foi possivel encontrar o arquivo");
		} catch (IOException e) {
			System.out.println("Nao foi possivel ler o arquivo");
		}
	}
	
	public String percorreAFD(String simbolo) {
		for(int i=0;i<regrastransicao.size();i++) {
			RegraTransicao regra = regrastransicao.get(i);
			if(regra.estadoinicial.equals(estadoAtual)
					&&regra.simbolos.contains(simbolo)) {
				estadoAtual = regra.estadofinal;
				return estadoAtual;
			}
		}
		return "";
	}
	
	public static void main(String[] args) {
		Asin analise = new Asin();
		analise.le_configAFD("configuracao.txt");
		analise.le_tabSimbolos("saida2.csv");

	}

}
