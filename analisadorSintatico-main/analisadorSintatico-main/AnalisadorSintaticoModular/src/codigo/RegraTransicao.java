package codigo;

public class RegraTransicao {
	String estadoinicial, simbolos, estadofinal;

	public String getEstadoinicial() {
		return estadoinicial;
	}

	public void setEstadoinicial(String estadoinicial) {
		this.estadoinicial = estadoinicial;
	}

	public String getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(String simbolos) {
		this.simbolos = simbolos;
	}

	public String getEstadofinal() {
		return estadofinal;
	}

	public void setEstadofinal(String estadofinal) {
		this.estadofinal = estadofinal;
	}

	public RegraTransicao(String a, String b, String c) {
		estadoinicial = a;
		simbolos = b;
		estadofinal = c;
	}
}
