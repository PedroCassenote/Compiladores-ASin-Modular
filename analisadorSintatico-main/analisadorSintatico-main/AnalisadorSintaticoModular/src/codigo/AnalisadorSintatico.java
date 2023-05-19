package codigo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AnalisadorSintatico {

	// termos guarda todos os tipos possiveis de tipos de tokens
	// para ser utilizados em regras de transicao
	LinkedList<String> termos = new LinkedList<String>();

	String[] nomesTiposTokens = { "NUMEROINT", "OPMATEMATICA", "VIRGULA", "PV", "NOME", "NUMERODECIMAL", "CHARVAL",
			"ESPACO", "ATRIBUICAO", "COMPARACAO", "abreParentesis", "fechaParentesis", "opLogico", "AChave", "FChave",
			"ACol", "FCol" };

	String[] pReservadas = { "auto", "else", "long", "switch", "break", "enum", "register", "typedef", "case", "extern",
			"return", "union", "char", "float", "short", "unsigned", "const", "for", "signed", "void", "continue",
			"goto", "sizeof", "volatile", "default", "if", "static", "while", "do", "int", "struct", "double" };

	HashMap<String, String> sintaxes = new HashMap<String, String>();

	String[] vetorTokens = { "IF", "abreParentesis", "NOME", "COMPARACAO", "NOME", "dsadasdasds", "PV", "fechaParentesis" };

	public void registraSintaxe(String nome, String arquivoConfig) {

		sintaxes.put(nome, arquivoConfig);
	}

	public void addTermos() {
		for (int j = 0; j < nomesTiposTokens.length; j++) {
			termos.add(nomesTiposTokens[j]);
		}
		for (int i = 0; i < pReservadas.length; i++) {
			termos.add(pReservadas[i].toUpperCase());
		}
	}

	// TODAS AS SINTAXES DEVEM SER CARREGADAS
	// ANTES DA EXECUCAO
	public AnalisadorSintatico() {
		// TODO Auto-generated constructor stub
		addTermos();
		registraSintaxe("SINTIF", "ifConfig.txt");
		registraSintaxe("SINTCOMP", "comparacaoConfig.txt");
	}

	public static void main(String[] args) {

		AnalisadorSintatico asin = new AnalisadorSintatico();
		/*
		 * AFD afd1 = new AFD("SINTCOMPARACAO", "comparacaoConfig.txt",
		 * vetorTokens,asin.termos); System.out.println(afd1.executa(2));
		 * System.out.println(afd1.executa(6));
		 */
		AFD afdIF = new AFD("SINTIF", "ifConfig.txt", asin.vetorTokens, asin.termos);
		afdIF.setSintaxes(asin.sintaxes);
		System.out.println(afdIF.executa(0));
	}

}
