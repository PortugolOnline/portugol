package br.com.vinyanalista.portugol.interpretador.analise;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.vinyanalista.portugol.base.node.*;
import br.com.vinyanalista.portugol.base.parser.Parser;
import br.com.vinyanalista.portugol.interpretador.simbolo.*;
import br.com.vinyanalista.portugol.interpretador.tipo.*;

public class AnalisadorDeDeclaracoesTeste {
	AnalisadorDeDeclaracoes analisadorDeDeclaracoes;
	AnalisadorSemantico analisadorSemantico;

	private void inicializar() {
		analisadorSemantico = new AnalisadorSemantico((Parser) null);
		// TODO Refatorar: alto nível de acoplamento entre as classes
		// AnalisadorDeDeclaracoes e AnalisadorSemantico
		analisadorDeDeclaracoes = new AnalisadorDeDeclaracoes(analisadorSemantico);
	}

	private void inserirErroSemantico() {
		analisadorSemantico.lancarErroSemantico(null, 1, 1, "Um erro semântico para teste");
	}

	private void removerErroSemantico() {
		inicializar();
	}

	@Before
	public void setUp() {
		inicializar();
	}

	@Test
	public void outADeclaracao() {
		//fail("Não implementado ainda");
	}

	@Test
	public void outASimplesVariavel() {
		// Entrada: n1
		String entradaIdentificadorComoString = "n1";
		int entradaLinha = 4, entradaColuna = 8;
		TIdentificador entradaIdentificador = new TIdentificador(entradaIdentificadorComoString, entradaLinha,
				entradaColuna);
		ASimplesVariavel entradaVariavel = new ASimplesVariavel(entradaIdentificador);

		// Teste com erro semântico
		inserirErroSemantico();
		analisadorDeDeclaracoes.outASimplesVariavel(entradaVariavel);
		TabelaDeAtributos saidaAtributosDaVariavel = analisadorSemantico.obterAtributos(entradaVariavel);
		assertNull(saidaAtributosDaVariavel);
		removerErroSemantico();

		// Teste com entrada válida
		analisadorDeDeclaracoes.outASimplesVariavel(entradaVariavel);
		saidaAtributosDaVariavel = analisadorSemantico.obterAtributos(entradaVariavel);
		assertNotNull(saidaAtributosDaVariavel);

		String saidaIdentificador = (String) saidaAtributosDaVariavel.obter(Atributo.ID);
		assertNotNull(saidaIdentificador);
		assertEquals(entradaIdentificadorComoString.toUpperCase(), saidaIdentificador);

		Simbolo saidaSimbolo = (Simbolo) saidaAtributosDaVariavel.obter(Atributo.SIMBOLO);
		assertNotNull(saidaSimbolo);
		assertEquals(Simbolo.obter(entradaIdentificadorComoString.toUpperCase()), saidaSimbolo);

		Integer saidaLinha = (Integer) saidaAtributosDaVariavel.obter(Atributo.LINHA);
		assertNotNull(saidaLinha);
		assertEquals(entradaLinha, (int) saidaLinha);

		Integer saidaColuna = (Integer) saidaAtributosDaVariavel.obter(Atributo.COLUNA);
		assertNotNull(saidaColuna);
		assertEquals(entradaColuna, (int) saidaColuna);

		String saidaString = (String) saidaAtributosDaVariavel.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(entradaIdentificadorComoString.toUpperCase(), saidaString);

		// TODO Testar outros atributos, ver enum Atributo (em todos os métodos)

		// Teste com entrada nula
		// try {
		// analisadorDeDeclaracoes.outASimplesVariavel(null);
		// } catch (NullPointerException e) {
		// // TODO Tratar entradas nulas (em todos os métodos)
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outAVetorOuMatrizVariavel() {
		// Entrada: num[9]
		String entradaIdentificadorComoString = "num";
		int entradaLinha = 6, entradaColuna = 2;
		TIdentificador entradaIdentificador = new TIdentificador(entradaIdentificadorComoString, entradaLinha,
				entradaColuna);
		List<PExpressao> entradaListaDeExpressoes = new ArrayList<PExpressao>();
		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("9", 6, 6))));
		AVetorOuMatrizVariavel entradaVetorOuMatriz = new AVetorOuMatrizVariavel(entradaIdentificador,
				entradaListaDeExpressoes);

		// Teste com erro semântico
		inserirErroSemantico();
		analisadorDeDeclaracoes.outAVetorOuMatrizVariavel(entradaVetorOuMatriz);
		TabelaDeAtributos saidaAtributosDoVetorOuMatriz = analisadorSemantico.obterAtributos(entradaVetorOuMatriz);
		assertNull(saidaAtributosDoVetorOuMatriz);
		removerErroSemantico();

		// Teste com entrada válida
		analisadorDeDeclaracoes.outAVetorOuMatrizVariavel(entradaVetorOuMatriz);
		saidaAtributosDoVetorOuMatriz = analisadorSemantico.obterAtributos(entradaVetorOuMatriz);
		assertNotNull(saidaAtributosDoVetorOuMatriz);

		String saidaIdentificador = (String) saidaAtributosDoVetorOuMatriz.obter(Atributo.ID);
		assertNotNull(saidaIdentificador);
		assertEquals(entradaIdentificadorComoString.toUpperCase(), saidaIdentificador);

		Simbolo saidaSimbolo = (Simbolo) saidaAtributosDoVetorOuMatriz.obter(Atributo.SIMBOLO);
		assertNotNull(saidaSimbolo);
		assertEquals(Simbolo.obter(entradaIdentificadorComoString.toUpperCase()), saidaSimbolo);

		Integer saidaLinha = (Integer) saidaAtributosDoVetorOuMatriz.obter(Atributo.LINHA);
		assertNotNull(saidaLinha);
		assertEquals(entradaLinha, (int) saidaLinha);

		Integer saidaColuna = (Integer) saidaAtributosDoVetorOuMatriz.obter(Atributo.COLUNA);
		assertNotNull(saidaColuna);
		assertEquals(entradaColuna, (int) saidaColuna);

		String saidaString = (String) saidaAtributosDoVetorOuMatriz.obter(Atributo.STRING);
		assertNull(saidaString);

		// Teste com entrada nula
		// try {
		// analisadorDeDeclaracoes.outAVetorOuMatrizVariavel(null);
		// } catch (NullPointerException e) {
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outANumericoTipo() {
		// Entrada: numerico
		ANumericoTipo entradaTipoNumerico = new ANumericoTipo();

		// Teste com erro semântico
		inserirErroSemantico();
		analisadorDeDeclaracoes.outANumericoTipo(entradaTipoNumerico);
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoNumerico);
		assertNull(saidaAtributosDoTipo);
		removerErroSemantico();

		// Teste com entrada válida
		analisadorDeDeclaracoes.outANumericoTipo(entradaTipoNumerico);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoNumerico);
		assertNotNull(saidaAtributosDoTipo);

		String saidaIdentificador = (String) saidaAtributosDoTipo.obter(Atributo.ID);
		assertNull(saidaIdentificador);

		Simbolo saidaSimbolo = (Simbolo) saidaAtributosDoTipo.obter(Atributo.SIMBOLO);
		assertNull(saidaSimbolo);

		Integer saidaLinha = (Integer) saidaAtributosDoTipo.obter(Atributo.LINHA);
		assertNull(saidaLinha);

		Integer saidaColuna = (Integer) saidaAtributosDoTipo.obter(Atributo.COLUNA);
		assertNull(saidaColuna);

		Tipo esperadoTipo = new Tipo(TipoPrimitivo.NUMERICO);

		Tipo saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		String saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoTipo.toString(), saidaString);

		// Teste com entrada nula
		// try {
		// analisadorDeDeclaracoes.outANumericoTipo(null);
		// } catch (NullPointerException e) {
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outALiteralTipo() {
		// Entrada: literal
		ALiteralTipo entradaTipoLiteral = new ALiteralTipo();

		// Teste com erro semântico
		inserirErroSemantico();
		analisadorDeDeclaracoes.outALiteralTipo(entradaTipoLiteral);
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoLiteral);
		assertNull(saidaAtributosDoTipo);
		removerErroSemantico();

		// Teste com entrada válida
		analisadorDeDeclaracoes.outALiteralTipo(entradaTipoLiteral);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoLiteral);
		assertNotNull(saidaAtributosDoTipo);

		String saidaIdentificador = (String) saidaAtributosDoTipo.obter(Atributo.ID);
		assertNull(saidaIdentificador);

		Simbolo saidaSimbolo = (Simbolo) saidaAtributosDoTipo.obter(Atributo.SIMBOLO);
		assertNull(saidaSimbolo);

		Integer saidaLinha = (Integer) saidaAtributosDoTipo.obter(Atributo.LINHA);
		assertNull(saidaLinha);

		Integer saidaColuna = (Integer) saidaAtributosDoTipo.obter(Atributo.COLUNA);
		assertNull(saidaColuna);

		Tipo esperadoTipo = new Tipo(TipoPrimitivo.LITERAL);

		Tipo saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		String saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoTipo.toString(), saidaString);

		// Teste com entrada nula
		// try {
		// analisadorDeDeclaracoes.outALiteralTipo(null);
		// } catch (NullPointerException e) {
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outALogicoTipo() {
		// Entrada: logico
		ALogicoTipo entradaTipoLogico = new ALogicoTipo();

		// Teste com erro semântico
		inserirErroSemantico();
		analisadorDeDeclaracoes.outALogicoTipo(entradaTipoLogico);
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoLogico);
		assertNull(saidaAtributosDoTipo);
		removerErroSemantico();

		// Teste com entrada válida
		analisadorDeDeclaracoes.outALogicoTipo(entradaTipoLogico);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoLogico);
		assertNotNull(saidaAtributosDoTipo);

		String saidaIdentificador = (String) saidaAtributosDoTipo.obter(Atributo.ID);
		assertNull(saidaIdentificador);

		Simbolo saidaSimbolo = (Simbolo) saidaAtributosDoTipo.obter(Atributo.SIMBOLO);
		assertNull(saidaSimbolo);

		Integer saidaLinha = (Integer) saidaAtributosDoTipo.obter(Atributo.LINHA);
		assertNull(saidaLinha);

		Integer saidaColuna = (Integer) saidaAtributosDoTipo.obter(Atributo.COLUNA);
		assertNull(saidaColuna);

		Tipo esperadoTipo = new Tipo(TipoPrimitivo.LOGICO);

		Tipo saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		String saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoTipo.toString(), saidaString);

		// Teste com entrada nula
		// try {
		// analisadorDeDeclaracoes.outALogicoTipo(null);
		// } catch (NullPointerException e) {
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outARegistroTipo() {
		// Entrada: registro (num, saldo numerico nome literal)
		String entradaCampoNumIdentificador = "num";
		int entradaLinha = 16, entradaCampoNumColuna = 22;
		PVariavel entradaCampoNum = new ASimplesVariavel(
				new TIdentificador(entradaCampoNumIdentificador, entradaLinha, entradaCampoNumColuna));

		String entradaCampoSaldoIdentificador = "saldo";
		int entradaCampoSaldoColuna = 27;
		PVariavel entradaCampoSaldo = new ASimplesVariavel(
				new TIdentificador(entradaCampoSaldoIdentificador, entradaLinha, entradaCampoSaldoColuna));

		List<PVariavel> auxiliarListaDeVariaveis = new ArrayList<PVariavel>();
		auxiliarListaDeVariaveis.add(entradaCampoNum);
		auxiliarListaDeVariaveis.add(entradaCampoSaldo);
		PDeclaracao entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeVariaveis, new ANumericoTipo());

		String entradaCampoNomeIdentificador = "nome";
		int entradaCampoNomeColuna = 42;
		PVariavel entradaCampoNome = new ASimplesVariavel(
				new TIdentificador(entradaCampoNomeIdentificador, entradaLinha, entradaCampoNomeColuna));

		auxiliarListaDeVariaveis = new ArrayList<PVariavel>();
		auxiliarListaDeVariaveis.add(entradaCampoNome);
		PDeclaracao entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeVariaveis, new ALiteralTipo());

		List<PDeclaracao> entradaListaDeDeclaracoes = new ArrayList<PDeclaracao>();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);
		entradaListaDeDeclaracoes.add(entradaDeclaracaoLiteral);
		ARegistroTipo entradaTipoRegistro = new ARegistroTipo(entradaListaDeDeclaracoes);

		// Teste com erro semântico
		inserirErroSemantico();
		try {
			analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		} catch (Exception e) {
			fail("Se havia erro semântico, a análise semântica não deveria ter sido executada");
		}
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNull(saidaAtributosDoTipo);
		removerErroSemantico();

		// Teste com entrada válida
		TabelaDeAtributos entradaAtributosDoCampoNum = new TabelaDeAtributos();
		entradaAtributosDoCampoNum.inserir(Atributo.ID, entradaCampoNumIdentificador.toUpperCase());
		Simbolo entradaCampoNumSimbolo = Simbolo.obter(entradaCampoNumIdentificador.toUpperCase());
		entradaAtributosDoCampoNum.inserir(Atributo.SIMBOLO, entradaCampoNumSimbolo);
		entradaAtributosDoCampoNum.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoNum.inserir(Atributo.COLUNA, entradaCampoNumColuna);
		entradaAtributosDoCampoNum.inserir(Atributo.STRING, entradaCampoNumIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoNum, entradaAtributosDoCampoNum);

		TabelaDeAtributos entradaAtributosDoCampoSaldo = new TabelaDeAtributos();
		entradaAtributosDoCampoSaldo.inserir(Atributo.ID, entradaCampoSaldoIdentificador.toUpperCase());
		Simbolo entradaCampoSaldoSimbolo = Simbolo.obter(entradaCampoSaldoIdentificador.toUpperCase());
		entradaAtributosDoCampoSaldo.inserir(Atributo.SIMBOLO, entradaCampoSaldoSimbolo);
		entradaAtributosDoCampoSaldo.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoSaldo.inserir(Atributo.COLUNA, entradaCampoSaldoColuna);
		entradaAtributosDoCampoSaldo.inserir(Atributo.STRING, entradaCampoSaldoIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoSaldo, entradaAtributosDoCampoSaldo);

		TabelaDeAtributos entradaAtributosDoCampoNome = new TabelaDeAtributos();
		entradaAtributosDoCampoNome.inserir(Atributo.ID, entradaCampoNomeIdentificador.toUpperCase());
		Simbolo entradaCampoNomeSimbolo = Simbolo.obter(entradaCampoNomeIdentificador.toUpperCase());
		entradaAtributosDoCampoNome.inserir(Atributo.SIMBOLO, entradaCampoNomeSimbolo);
		entradaAtributosDoCampoNome.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoNome.inserir(Atributo.COLUNA, entradaCampoNumColuna);
		entradaAtributosDoCampoNome.inserir(Atributo.STRING, entradaCampoNomeIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoNome, entradaAtributosDoCampoNome);

		analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNotNull(saidaAtributosDoTipo);

		TipoRegistro esperadoTipo = new TipoRegistro();
		esperadoTipo.getCampos().inserir(entradaCampoNumSimbolo, entradaAtributosDoCampoNum);
		esperadoTipo.getCampos().inserir(entradaCampoSaldoSimbolo, entradaAtributosDoCampoSaldo);
		esperadoTipo.getCampos().inserir(entradaCampoNomeSimbolo, entradaAtributosDoCampoNome);

		Tipo saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		// TODO TabelaDeSimbolos.equals() não está implementado
		// assertEquals(esperadoTipo, saidaTipo);

		String saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		// TODO TipoRegistro.toString() não está implementado
		assertEquals("REGISTRO([...])", saidaString);

		// Teste com campo repetido (erro semântico)
		// Entrada: registro (num, saldo numerico nome, nome literal)

		int entradaCampoRepetidoColuna = 48;
		PVariavel entradaCampoRepetido = new ASimplesVariavel(
				new TIdentificador(entradaCampoNomeIdentificador, entradaLinha, entradaCampoRepetidoColuna));

		auxiliarListaDeVariaveis.add(entradaCampoRepetido);
		entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeVariaveis, new ALiteralTipo());

		entradaListaDeDeclaracoes = new ArrayList<PDeclaracao>();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);
		entradaListaDeDeclaracoes.add(entradaDeclaracaoLiteral);
		entradaTipoRegistro = new ARegistroTipo(entradaListaDeDeclaracoes);

		TabelaDeAtributos entradaAtributosDoCampoRepetido = new TabelaDeAtributos();
		entradaAtributosDoCampoRepetido.inserir(Atributo.ID, entradaCampoNomeIdentificador.toUpperCase());
		Simbolo entradaCampoRepetidoSimbolo = Simbolo.obter(entradaCampoNomeIdentificador.toUpperCase());
		entradaAtributosDoCampoRepetido.inserir(Atributo.SIMBOLO, entradaCampoRepetidoSimbolo);
		entradaAtributosDoCampoRepetido.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoRepetido.inserir(Atributo.COLUNA, entradaCampoRepetidoColuna);
		entradaAtributosDoCampoRepetido.inserir(Atributo.STRING, entradaCampoNomeIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoRepetido, entradaAtributosDoCampoRepetido);

		analisadorSemantico.getTabelasDeAtributos().remove(entradaTipoRegistro);

		// TODO AnalisadorSemantico.analisar() throws ErroSemantico, seria o caso de
		// refatorar o método AnalisadorSemantico.lancarErroSemantico()
		analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNull(saidaAtributosDoTipo);
		assertTrue(analisadorSemantico.haErroSemantico());
	}

	@Test
	public void caseASubRotina() {
		//fail("Não implementado ainda");
	}
}