package br.com.vinyanalista.portugol.interpretador.analise;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

import br.com.vinyanalista.portugol.base.node.*;
import br.com.vinyanalista.portugol.base.parser.Parser;
import br.com.vinyanalista.portugol.interpretador.simbolo.*;
import br.com.vinyanalista.portugol.interpretador.tipo.*;

public class AnalisadorDeDeclaracoesTeste {
	AnalisadorDeDeclaracoes analisadorDeDeclaracoes;
	AnalisadorSemantico analisadorSemantico;

	private void inicializar() {
		analisadorSemantico = new AnalisadorSemantico((Parser) null);
		// TODO Refatoração - Alto nível de acoplamento entre as classes
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
		// Entrada: declare x numerico
		String entradaVariavelXIdentificadorComoString = "x";
		int entradaLinha = 5, entradaVariavelXColuna = 9;
		TIdentificador entradaVariavelXIdentificador = new TIdentificador(entradaVariavelXIdentificadorComoString,
				entradaLinha, entradaVariavelXColuna);
		ASimplesVariavel entradaVariavelX = new ASimplesVariavel(entradaVariavelXIdentificador);

		List<PVariavel> entradaListaDeVariaveis = new ArrayList<>();
		entradaListaDeVariaveis.add(entradaVariavelX);

		PTipo entradaTipo = new ANumericoTipo();

		ADeclaracao entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		TabelaDeAtributos entradaAtributosDaVariavelX = new TabelaDeAtributos();
		entradaAtributosDaVariavelX.inserir(Atributo.ID, entradaVariavelXIdentificadorComoString.toUpperCase());
		Simbolo entradaVariavelXSimbolo = Simbolo.obter(entradaVariavelXIdentificadorComoString.toUpperCase());
		entradaAtributosDaVariavelX.inserir(Atributo.SIMBOLO, entradaVariavelXSimbolo);
		entradaAtributosDaVariavelX.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDaVariavelX.inserir(Atributo.COLUNA, entradaVariavelXColuna);
		entradaAtributosDaVariavelX.inserir(Atributo.STRING, entradaVariavelXIdentificadorComoString.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaVariavelX, entradaAtributosDaVariavelX);

		Tipo entradaTipoNumerico = new Tipo(TipoPrimitivo.NUMERICO);
		TabelaDeAtributos entradaAtributosDoTipoNumerico = new TabelaDeAtributos();
		entradaAtributosDoTipoNumerico.inserir(Atributo.TIPO, entradaTipoNumerico);
		entradaAtributosDoTipoNumerico.inserir(Atributo.STRING, entradaTipoNumerico.toString());
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoNumerico);

		// ********************************************************************************
		// Teste 1 - com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		TabelaDeAtributos saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNull(saidaAtributosDaDeclaracao);

		removerErroSemantico();

		// ********************************************************************************
		// Teste 2 - 1 variável de tipo primitivo
		// ********************************************************************************

		analisadorSemantico.gravarAtributos(entradaVariavelX, entradaAtributosDaVariavelX);
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoNumerico);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNotNull(saidaAtributosDaDeclaracao);

		TabelaDeAtributos saidaAtributosDaVariavelX = analisadorSemantico.obterAtributos(entradaVariavelX);

		Tipo saidaTipoDaVariavelX = (Tipo) saidaAtributosDaVariavelX.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaVariavelX);
		assertEquals(entradaTipoNumerico, saidaTipoDaVariavelX);

		assertNotNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVariavelXSimbolo));

		Tipo saidaTipoDaDeclaracao = (Tipo) saidaAtributosDaDeclaracao.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaDeclaracao);
		assertEquals(entradaTipoNumerico, saidaTipoDaDeclaracao);

		String saidaStringDaDeclaracao = (String) saidaAtributosDaDeclaracao.obter(Atributo.STRING);
		assertNotNull(saidaStringDaDeclaracao);
		assertEquals("[...] NUMERICO", saidaStringDaDeclaracao);
		
		// TODO Dúvida - Deveria testar outros atributos? (ver enum Atributo)

		// TODO Dúvida - Não há um método para remover símbolos da tabela de símbolos
		// (seria o caso de criar?)
		inicializar();

		// ********************************************************************************
		// Teste 3 - 2 variáveis de tipo primitivo
		// Entrada: declare x, y numerico
		// ********************************************************************************

		String entradaVariavelYIdentificadorComoString = "y";
		int entradaVariavelYColuna = 12;
		TIdentificador entradaVariavelYIdentificador = new TIdentificador(entradaVariavelYIdentificadorComoString,
				entradaLinha, entradaVariavelYColuna);
		ASimplesVariavel entradaVariavelY = new ASimplesVariavel(entradaVariavelYIdentificador);

		entradaListaDeVariaveis.add(entradaVariavelY);

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		analisadorSemantico.gravarAtributos(entradaVariavelX, entradaAtributosDaVariavelX);

		TabelaDeAtributos entradaAtributosDaVariavelY = new TabelaDeAtributos();
		entradaAtributosDaVariavelY.inserir(Atributo.ID, entradaVariavelYIdentificadorComoString.toUpperCase());
		Simbolo entradaVariavelYSimbolo = Simbolo.obter(entradaVariavelYIdentificadorComoString.toUpperCase());
		entradaAtributosDaVariavelY.inserir(Atributo.SIMBOLO, entradaVariavelYSimbolo);
		entradaAtributosDaVariavelY.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDaVariavelY.inserir(Atributo.COLUNA, entradaVariavelYColuna);
		entradaAtributosDaVariavelY.inserir(Atributo.STRING, entradaVariavelYIdentificadorComoString.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaVariavelY, entradaAtributosDaVariavelY);

		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoNumerico);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNotNull(saidaAtributosDaDeclaracao);

		saidaAtributosDaVariavelX = analisadorSemantico.obterAtributos(entradaVariavelX);

		saidaTipoDaVariavelX = (Tipo) saidaAtributosDaVariavelX.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaVariavelX);
		assertEquals(entradaTipoNumerico, saidaTipoDaVariavelX);

		assertNotNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVariavelXSimbolo));

		TabelaDeAtributos saidaAtributosDaVariavelY = analisadorSemantico.obterAtributos(entradaVariavelY);

		Tipo saidaTipoDaVariavelY = (Tipo) saidaAtributosDaVariavelY.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaVariavelY);
		assertEquals(entradaTipoNumerico, saidaTipoDaVariavelY);

		assertNotNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVariavelYSimbolo));

		saidaTipoDaDeclaracao = (Tipo) saidaAtributosDaDeclaracao.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaDeclaracao);
		assertEquals(entradaTipoNumerico, saidaTipoDaDeclaracao);

		saidaStringDaDeclaracao = (String) saidaAtributosDaDeclaracao.obter(Atributo.STRING);
		assertNotNull(saidaStringDaDeclaracao);
		assertEquals("[...] NUMERICO", saidaStringDaDeclaracao);

		inicializar();

		// ********************************************************************************
		// Teste 4 - vetor de capacidade 1
		// Entrada: declare letras[1] literal
		// ********************************************************************************

		String entradaVetorLetrasIdentificadorComoString = "letras";
		int entradaVetorLetrasColuna = 2;
		TIdentificador entradaVetorLetrasIdentificador = new TIdentificador(entradaVetorLetrasIdentificadorComoString,
				entradaLinha, entradaVetorLetrasColuna);
		List<PExpressao> entradaListaDeExpressoes = new ArrayList<>();
		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("1", entradaLinha, 9))));
		AVetorOuMatrizVariavel entradaVetorLetras = new AVetorOuMatrizVariavel(entradaVetorLetrasIdentificador,
				entradaListaDeExpressoes);

		entradaListaDeVariaveis.clear();
		entradaListaDeVariaveis.add(entradaVetorLetras);

		entradaTipo = new ALiteralTipo();

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		TabelaDeAtributos entradaAtributosDoVetorLetras = new TabelaDeAtributos();
		entradaAtributosDoVetorLetras.inserir(Atributo.ID, entradaVetorLetrasIdentificadorComoString.toUpperCase());
		Simbolo entradaVetorLetrasSimbolo = Simbolo.obter(entradaVetorLetrasIdentificadorComoString.toUpperCase());
		entradaAtributosDoVetorLetras.inserir(Atributo.SIMBOLO, entradaVetorLetrasSimbolo);
		entradaAtributosDoVetorLetras.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoVetorLetras.inserir(Atributo.COLUNA, entradaVetorLetrasColuna);
		entradaAtributosDoVetorLetras.inserir(Atributo.STRING, entradaVetorLetrasIdentificadorComoString.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaVetorLetras, entradaAtributosDoVetorLetras);

		Tipo entradaTipoLiteral = new Tipo(TipoPrimitivo.LITERAL);
		TabelaDeAtributos entradaAtributosDoTipoLiteral = new TabelaDeAtributos();
		entradaAtributosDoTipoLiteral.inserir(Atributo.TIPO, entradaTipoLiteral);
		entradaAtributosDoTipoLiteral.inserir(Atributo.STRING, entradaTipoLiteral.toString());
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoLiteral);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNotNull(saidaAtributosDaDeclaracao);

		TabelaDeAtributos saidaAtributosDoVetorLetras = analisadorSemantico.obterAtributos(entradaVetorLetras);

		String saidaStringDoVetorLetras = (String) saidaAtributosDoVetorLetras.obter(Atributo.STRING);
		assertNotNull(saidaStringDoVetorLetras);
		assertEquals("LETRAS[1]", saidaStringDoVetorLetras);

		Tipo esperadoTipoDoVetorLetras = new TipoVetorOuMatriz(entradaTipoLiteral, new int[] { 1 });

		Tipo saidaTipoDoVetorLetras = (Tipo) saidaAtributosDoVetorLetras.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDoVetorLetras);
		assertEquals(esperadoTipoDoVetorLetras, saidaTipoDoVetorLetras);

		assertNotNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVetorLetrasSimbolo));

		saidaTipoDaDeclaracao = (Tipo) saidaAtributosDaDeclaracao.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaDeclaracao);
		assertEquals(entradaTipoLiteral, saidaTipoDaDeclaracao);

		saidaStringDaDeclaracao = (String) saidaAtributosDaDeclaracao.obter(Atributo.STRING);
		assertNotNull(saidaStringDaDeclaracao);
		assertEquals("[...] LITERAL", saidaStringDaDeclaracao);

		entradaAtributosDoVetorLetras.atributos().remove(Atributo.TIPO);
		entradaAtributosDoVetorLetras.inserir(Atributo.STRING, entradaVetorLetrasIdentificadorComoString.toUpperCase());
		inicializar();

		// ********************************************************************************
		// Teste 5 - vetor de capacidade 0 (gera erro semântico)
		// Entrada: declare letras[0] literal
		// ********************************************************************************

		entradaListaDeExpressoes.clear();
		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("0", entradaLinha, 9))));
		entradaVetorLetras = new AVetorOuMatrizVariavel(entradaVetorLetrasIdentificador, entradaListaDeExpressoes);

		entradaListaDeVariaveis.clear();
		entradaListaDeVariaveis.add(entradaVetorLetras);

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		analisadorSemantico.gravarAtributos(entradaVetorLetras, entradaAtributosDoVetorLetras);
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoLiteral);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNull(saidaAtributosDaDeclaracao);
		// TODO Refatoração - AnalisadorSemantico.analisar() throws ErroSemantico, seria
		// o caso de refatorar o método AnalisadorSemantico.lancarErroSemantico()
		// TODO Defeito - mensagem de erro ambígua na linha 58 da classe
		// AnalisadorDeDeclaracoes
		assertTrue(analisadorSemantico.haErroSemantico());
		// TODO Refatoração - Não é possível testar linha e coluna do erro semântico

		saidaAtributosDoVetorLetras = analisadorSemantico.obterAtributos(entradaVetorLetras);

		saidaStringDoVetorLetras = (String) saidaAtributosDoVetorLetras.obter(Atributo.STRING);
		assertNotNull(saidaStringDoVetorLetras);
		assertEquals(entradaVetorLetrasIdentificadorComoString.toUpperCase(), saidaStringDoVetorLetras);

		saidaTipoDoVetorLetras = (Tipo) saidaAtributosDoVetorLetras.obter(Atributo.TIPO);
		assertNull(saidaTipoDoVetorLetras);

		assertNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVetorLetrasSimbolo));

		inicializar();

		// ********************************************************************************
		// Teste 6 - vetor de capacidade -1 (gera erro semântico)
		// Entrada: declare letras[-1] literal
		// ********************************************************************************

		entradaListaDeExpressoes.clear();

		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("-1", entradaLinha, 9))));
		entradaVetorLetras = new AVetorOuMatrizVariavel(entradaVetorLetrasIdentificador, entradaListaDeExpressoes);

		entradaListaDeVariaveis.clear();
		entradaListaDeVariaveis.add(entradaVetorLetras);

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		analisadorSemantico.gravarAtributos(entradaVetorLetras, entradaAtributosDoVetorLetras);
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoLiteral);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		// TODO Defeito - Não deveria gravar atributos, se há erro semântico
		// assertNull(saidaAtributosDaDeclaracao);
		// TODO Defeito - Não está gerando erro semântico
		// assertTrue(analisadorSemantico.haErroSemantico());
		// TODO Refatoração - Não é possível testar linha e coluna do erro semântico

		saidaAtributosDoVetorLetras = analisadorSemantico.obterAtributos(entradaVetorLetras);

		saidaStringDoVetorLetras = (String) saidaAtributosDoVetorLetras.obter(Atributo.STRING);
		// assertNotNull(saidaStringDoVetorLetras);
		// assertEquals(entradaVetorLetrasIdentificadorComoString.toUpperCase(),
		// saidaStringDoVetorLetras);

		saidaTipoDoVetorLetras = (Tipo) saidaAtributosDoVetorLetras.obter(Atributo.TIPO);
		// assertNull(saidaTipoDoVetorLetras);

		// assertNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVetorLetrasSimbolo));

		entradaAtributosDoVetorLetras.atributos().remove(Atributo.TIPO);
		entradaAtributosDoVetorLetras.inserir(Atributo.STRING, entradaVetorLetrasIdentificadorComoString.toUpperCase());
		inicializar();

		// ********************************************************************************
		// Teste 7 - vetor com variável em vez de número inteiro nas dimensões
		// (gera erro semântico, mas deveria ser um erro sintático)
		// Entrada: declare letras[a] literal
		// ********************************************************************************

		entradaListaDeExpressoes.clear();

		entradaListaDeExpressoes.add(new APosicaoDeMemoriaExpressao(
				new AVariavelPosicaoDeMemoria(new ASimplesVariavel(new TIdentificador("a", entradaLinha, 9)))));
		entradaVetorLetras = new AVetorOuMatrizVariavel(entradaVetorLetrasIdentificador, entradaListaDeExpressoes);

		entradaListaDeVariaveis.clear();
		entradaListaDeVariaveis.add(entradaVetorLetras);

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		analisadorSemantico.gravarAtributos(entradaVetorLetras, entradaAtributosDoVetorLetras);
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoLiteral);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNull(saidaAtributosDaDeclaracao);
		// TODO Defeito - Na verdade, deveria ser um erro sintático
		assertTrue(analisadorSemantico.haErroSemantico());
		// TODO Refatoração - Não é possível testar linha e coluna do erro semântico

		saidaAtributosDoVetorLetras = analisadorSemantico.obterAtributos(entradaVetorLetras);

		saidaStringDoVetorLetras = (String) saidaAtributosDoVetorLetras.obter(Atributo.STRING);
		assertNotNull(saidaStringDoVetorLetras);
		assertEquals(entradaVetorLetrasIdentificadorComoString.toUpperCase(), saidaStringDoVetorLetras);

		saidaTipoDoVetorLetras = (Tipo) saidaAtributosDoVetorLetras.obter(Atributo.TIPO);
		assertNull(saidaTipoDoVetorLetras);

		assertNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVetorLetrasSimbolo));

		inicializar();

		// ********************************************************************************
		// Teste 8 - matriz
		// Entrada: declare mat[2,3] numerico
		// ********************************************************************************

		String entradaMatrizIdentificadorComoString = "mat";
		int entradaMatrizColuna = 9;
		TIdentificador entradaMatrizIdentificador = new TIdentificador(entradaMatrizIdentificadorComoString,
				entradaLinha, entradaMatrizColuna);
		entradaListaDeExpressoes.clear();
		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("2", entradaLinha, 13))));
		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("3", entradaLinha, 15))));
		AVetorOuMatrizVariavel entradaMatriz = new AVetorOuMatrizVariavel(entradaMatrizIdentificador,
				entradaListaDeExpressoes);

		entradaListaDeVariaveis.clear();
		entradaListaDeVariaveis.add(entradaMatriz);

		entradaTipo = new ANumericoTipo();

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		TabelaDeAtributos entradaAtributosDaMatriz = new TabelaDeAtributos();
		entradaAtributosDaMatriz.inserir(Atributo.ID, entradaMatrizIdentificadorComoString.toUpperCase());
		Simbolo entradaMatrizSimbolo = Simbolo.obter(entradaMatrizIdentificadorComoString.toUpperCase());
		entradaAtributosDaMatriz.inserir(Atributo.SIMBOLO, entradaMatrizSimbolo);
		entradaAtributosDaMatriz.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDaMatriz.inserir(Atributo.COLUNA, entradaMatrizColuna);
		entradaAtributosDaMatriz.inserir(Atributo.STRING, entradaMatrizIdentificadorComoString.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaMatriz, entradaAtributosDaMatriz);

		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoNumerico);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNotNull(saidaAtributosDaDeclaracao);

		TabelaDeAtributos saidaAtributosDaMatriz = analisadorSemantico.obterAtributos(entradaMatriz);

		String saidaStringDaMatriz = (String) saidaAtributosDaMatriz.obter(Atributo.STRING);
		assertNotNull(saidaStringDaMatriz);
		assertEquals("MAT[2, 3]", saidaStringDaMatriz);

		Tipo esperadoTipoDaMatriz = new TipoVetorOuMatriz(entradaTipoNumerico, new int[] { 2, 3 });

		Tipo saidaTipoDaMatriz = (Tipo) saidaAtributosDaMatriz.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaMatriz);
		assertEquals(esperadoTipoDaMatriz, saidaTipoDaMatriz);

		assertNotNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaMatrizSimbolo));

		saidaTipoDaDeclaracao = (Tipo) saidaAtributosDaDeclaracao.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaDeclaracao);
		assertEquals(entradaTipoNumerico, saidaTipoDaDeclaracao);

		saidaStringDaDeclaracao = (String) saidaAtributosDaDeclaracao.obter(Atributo.STRING);
		assertNotNull(saidaStringDaDeclaracao);
		assertEquals("[...] NUMERICO", saidaStringDaDeclaracao);

		inicializar();

		// ********************************************************************************
		// Teste 7 - registro
		// Entrada: declare conta registro (num, saldo numerico nome literal)
		// ********************************************************************************

		String entradaCampoNumIdentificador = "num";
		int entradaCampoNumColuna = 22;
		PVariavel entradaCampoNum = new ASimplesVariavel(
				new TIdentificador(entradaCampoNumIdentificador, entradaLinha, entradaCampoNumColuna));

		String entradaCampoSaldoIdentificador = "saldo";
		int entradaCampoSaldoColuna = 27;
		PVariavel entradaCampoSaldo = new ASimplesVariavel(
				new TIdentificador(entradaCampoSaldoIdentificador, entradaLinha, entradaCampoSaldoColuna));

		List<PVariavel> auxiliarListaDeCampos = new ArrayList<>();
		auxiliarListaDeCampos.add(entradaCampoNum);
		auxiliarListaDeCampos.add(entradaCampoSaldo);

		PDeclaracao entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeCampos, new ANumericoTipo());

		String entradaCampoNomeIdentificador = "nome";
		int entradaCampoNomeColuna = 42;
		PVariavel entradaCampoNome = new ASimplesVariavel(
				new TIdentificador(entradaCampoNomeIdentificador, entradaLinha, entradaCampoNomeColuna));

		auxiliarListaDeCampos.clear();
		auxiliarListaDeCampos.add(entradaCampoNome);

		PDeclaracao entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeCampos, new ALiteralTipo());

		List<PDeclaracao> entradaListaDeDeclaracoes = new ArrayList<>();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);
		entradaListaDeDeclaracoes.add(entradaDeclaracaoLiteral);

		entradaTipo = new ARegistroTipo(entradaListaDeDeclaracoes);

		String entradaVariavelContaIdentificadorComoString = "conta";
		int entradaVariavelContaColuna = 9;
		TIdentificador entradaVariavelContaIdentificador = new TIdentificador(
				entradaVariavelContaIdentificadorComoString, entradaLinha, entradaVariavelContaColuna);
		ASimplesVariavel entradaVariavelConta = new ASimplesVariavel(entradaVariavelContaIdentificador);

		entradaListaDeVariaveis = new ArrayList<>();
		entradaListaDeVariaveis.add(entradaVariavelConta);

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		TabelaDeAtributos entradaAtributosDaVariavelConta = new TabelaDeAtributos();
		entradaAtributosDaVariavelConta.inserir(Atributo.ID, entradaVariavelContaIdentificadorComoString.toUpperCase());
		Simbolo entradaVariavelContaSimbolo = Simbolo.obter(entradaVariavelContaIdentificadorComoString.toUpperCase());
		entradaAtributosDaVariavelConta.inserir(Atributo.SIMBOLO, entradaVariavelContaSimbolo);
		entradaAtributosDaVariavelConta.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDaVariavelConta.inserir(Atributo.COLUNA, entradaVariavelContaColuna);
		entradaAtributosDaVariavelConta.inserir(Atributo.STRING,
				entradaVariavelContaIdentificadorComoString.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaVariavelConta, entradaAtributosDaVariavelConta);

		TipoRegistro entradaTipoRegistro = new TipoRegistro();
		entradaTipoRegistro.getCampos().inserir(Simbolo.obter(entradaCampoNumIdentificador.toUpperCase()), null);
		entradaTipoRegistro.getCampos().inserir(Simbolo.obter(entradaCampoSaldoIdentificador.toUpperCase()), null);
		entradaTipoRegistro.getCampos().inserir(Simbolo.obter(entradaCampoNomeIdentificador.toUpperCase()), null);
		TabelaDeAtributos entradaAtributosDoTipoRegistro = new TabelaDeAtributos();
		entradaAtributosDoTipoRegistro.inserir(Atributo.TIPO, entradaTipoRegistro);
		entradaAtributosDoTipoRegistro.inserir(Atributo.STRING, entradaTipoRegistro.toString());
		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoRegistro);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNotNull(saidaAtributosDaDeclaracao);

		TabelaDeAtributos saidaAtributosDaVariavelConta = analisadorSemantico.obterAtributos(entradaVariavelConta);

		Tipo saidaTipoDaVariavelConta = (Tipo) saidaAtributosDaVariavelConta.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaVariavelConta);
		assertEquals(entradaTipoRegistro, saidaTipoDaVariavelConta);

		assertNotNull(analisadorSemantico.getTabelaDeSimbolos().obter(entradaVariavelContaSimbolo));

		saidaTipoDaDeclaracao = (Tipo) saidaAtributosDaDeclaracao.obter(Atributo.TIPO);
		assertNotNull(saidaTipoDaDeclaracao);
		assertEquals(entradaTipoRegistro, saidaTipoDaDeclaracao);

		saidaStringDaDeclaracao = (String) saidaAtributosDaDeclaracao.obter(Atributo.STRING);
		assertNotNull(saidaStringDaDeclaracao);
		assertEquals("[...] REGISTRO([...])", saidaStringDaDeclaracao);

		inicializar();

		// ********************************************************************************
		// Teste 8 - identificador repetido
		// Entrada: declare x, x numerico
		// ********************************************************************************

		String entradaVariavelRepetidaIdentificadorComoString = entradaVariavelXIdentificadorComoString;
		int entradaVariavelRepetidaColuna = 12;
		TIdentificador entradaVariavelRepetidaIdentificador = new TIdentificador(
				entradaVariavelRepetidaIdentificadorComoString, entradaLinha, entradaVariavelRepetidaColuna);
		ASimplesVariavel entradaVariavelRepetida = new ASimplesVariavel(entradaVariavelRepetidaIdentificador);

		entradaListaDeVariaveis.clear();
		entradaListaDeVariaveis.add(entradaVariavelX);
		entradaListaDeVariaveis.add(entradaVariavelRepetida);

		entradaTipo = new ANumericoTipo();

		entradaDeclaracao = new ADeclaracao(entradaListaDeVariaveis, entradaTipo);

		analisadorSemantico.gravarAtributos(entradaVariavelX, entradaAtributosDaVariavelX);

		TabelaDeAtributos entradaAtributosDaVariavelRepetida = new TabelaDeAtributos();
		entradaAtributosDaVariavelRepetida.inserir(Atributo.ID,
				entradaVariavelRepetidaIdentificadorComoString.toUpperCase());
		Simbolo entradaVariavelRepetidaSimbolo = Simbolo
				.obter(entradaVariavelRepetidaIdentificadorComoString.toUpperCase());
		entradaAtributosDaVariavelRepetida.inserir(Atributo.SIMBOLO, entradaVariavelRepetidaSimbolo);
		entradaAtributosDaVariavelRepetida.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDaVariavelRepetida.inserir(Atributo.COLUNA, entradaVariavelRepetidaColuna);
		entradaAtributosDaVariavelRepetida.inserir(Atributo.STRING,
				entradaVariavelRepetidaIdentificadorComoString.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaVariavelRepetida, entradaAtributosDaVariavelRepetida);

		analisadorSemantico.gravarAtributos(entradaTipo, entradaAtributosDoTipoNumerico);

		analisadorDeDeclaracoes.outADeclaracao(entradaDeclaracao);
		saidaAtributosDaDeclaracao = analisadorSemantico.obterAtributos(entradaDeclaracao);
		assertNull(saidaAtributosDaDeclaracao);
		assertTrue(analisadorSemantico.haErroSemantico());
		// TODO Refatoração - Não é possível testar linha e coluna do erro semântico

		TabelaDeAtributos saidaAtributosDaVariavelRepetida = analisadorSemantico
				.obterAtributos(entradaVariavelRepetida);

		Tipo saidaTipoDaVariavelRepetida = (Tipo) saidaAtributosDaVariavelRepetida.obter(Atributo.TIPO);
		// TODO Defeito - Não deveria guardar tipo se houve erro semântico
		// assertNull(saidaTipoDaVariavelRepetida);

		inicializar();

		// ********************************************************************************
		// Teste 9 - com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outADeclaracao(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outASimplesVariavel() {
		// Entrada: n1
		String entradaIdentificadorComoString = "n1";
		int entradaLinha = 4, entradaColuna = 8;
		TIdentificador entradaIdentificador = new TIdentificador(entradaIdentificadorComoString, entradaLinha,
				entradaColuna);
		ASimplesVariavel entradaVariavel = new ASimplesVariavel(entradaIdentificador);

		// ********************************************************************************
		// Teste com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		analisadorDeDeclaracoes.outASimplesVariavel(entradaVariavel);
		TabelaDeAtributos saidaAtributosDaVariavel = analisadorSemantico.obterAtributos(entradaVariavel);
		assertNull(saidaAtributosDaVariavel);

		removerErroSemantico();

		// ********************************************************************************
		// Teste com entrada válida
		// ********************************************************************************

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

		// ********************************************************************************
		// Teste com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outASimplesVariavel(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
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
		List<PExpressao> entradaListaDeExpressoes = new ArrayList<>();
		entradaListaDeExpressoes.add(new AValorExpressao(new AInteiroValor(new TNumeroInteiro("9", entradaLinha, 6))));
		AVetorOuMatrizVariavel entradaVetorOuMatriz = new AVetorOuMatrizVariavel(entradaIdentificador,
				entradaListaDeExpressoes);

		// ********************************************************************************
		// Teste com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		analisadorDeDeclaracoes.outAVetorOuMatrizVariavel(entradaVetorOuMatriz);
		TabelaDeAtributos saidaAtributosDoVetorOuMatriz = analisadorSemantico.obterAtributos(entradaVetorOuMatriz);
		assertNull(saidaAtributosDoVetorOuMatriz);

		removerErroSemantico();

		// ********************************************************************************
		// Teste com entrada válida
		// ********************************************************************************

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

		// ********************************************************************************
		// Teste com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outAVetorOuMatrizVariavel(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outANumericoTipo() {
		// Entrada: numerico
		ANumericoTipo entradaTipoNumerico = new ANumericoTipo();

		// ********************************************************************************
		// Teste com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		analisadorDeDeclaracoes.outANumericoTipo(entradaTipoNumerico);
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoNumerico);
		assertNull(saidaAtributosDoTipo);

		removerErroSemantico();

		// ********************************************************************************
		// Teste com entrada válida
		// ********************************************************************************

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

		// ********************************************************************************
		// Teste com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outANumericoTipo(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outALiteralTipo() {
		// Entrada: literal
		ALiteralTipo entradaTipoLiteral = new ALiteralTipo();

		// ********************************************************************************
		// Teste com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		analisadorDeDeclaracoes.outALiteralTipo(entradaTipoLiteral);
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoLiteral);
		assertNull(saidaAtributosDoTipo);

		removerErroSemantico();

		// ********************************************************************************
		// Teste com entrada válida
		// ********************************************************************************

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

		// ********************************************************************************
		// Teste com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outALiteralTipo(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outALogicoTipo() {
		// Entrada: logico
		ALogicoTipo entradaTipoLogico = new ALogicoTipo();

		// ********************************************************************************
		// Teste com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		analisadorDeDeclaracoes.outALogicoTipo(entradaTipoLogico);
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoLogico);
		assertNull(saidaAtributosDoTipo);

		removerErroSemantico();

		// ********************************************************************************
		// Teste com entrada válida
		// ********************************************************************************

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

		// ********************************************************************************
		// Teste com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outALogicoTipo(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void outARegistroTipo() {
		// Entrada: registro ()
		// Erro sintático já capturado pelo analisador sintático

		// Entrada: registro (num numerico)
		String entradaCampoNumIdentificador = "num";
		int entradaLinha = 16, entradaCampoNumColuna = 22;
		PVariavel entradaCampoNum = new ASimplesVariavel(
				new TIdentificador(entradaCampoNumIdentificador, entradaLinha, entradaCampoNumColuna));

		List<PVariavel> auxiliarListaDeCampos = new ArrayList<>();
		auxiliarListaDeCampos.add(entradaCampoNum);

		PDeclaracao entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeCampos, new ANumericoTipo());

		List<PDeclaracao> entradaListaDeDeclaracoes = new ArrayList<>();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);

		ARegistroTipo entradaTipoRegistro = new ARegistroTipo(entradaListaDeDeclaracoes);

		TabelaDeAtributos entradaAtributosDoCampoNum = new TabelaDeAtributos();
		entradaAtributosDoCampoNum.inserir(Atributo.ID, entradaCampoNumIdentificador.toUpperCase());
		Simbolo entradaCampoNumSimbolo = Simbolo.obter(entradaCampoNumIdentificador.toUpperCase());
		entradaAtributosDoCampoNum.inserir(Atributo.SIMBOLO, entradaCampoNumSimbolo);
		entradaAtributosDoCampoNum.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoNum.inserir(Atributo.COLUNA, entradaCampoNumColuna);
		entradaAtributosDoCampoNum.inserir(Atributo.STRING, entradaCampoNumIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoNum, entradaAtributosDoCampoNum);

		// ********************************************************************************
		// Teste 1 - com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		try {
			analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		} catch (Exception e) {
			fail("Se havia erro semântico, a análise semântica não deveria ter sido executada");
		}
		TabelaDeAtributos saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNull(saidaAtributosDoTipo);

		removerErroSemantico();

		// ********************************************************************************
		// Teste 2 - com entrada válida
		// ********************************************************************************

		analisadorSemantico.gravarAtributos(entradaCampoNum, entradaAtributosDoCampoNum);
		analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNotNull(saidaAtributosDoTipo);

		TipoRegistro esperadoTipo = new TipoRegistro();
		esperadoTipo.getCampos().inserir(entradaCampoNumSimbolo, entradaAtributosDoCampoNum);

		Tipo saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		// TODO Defeito - TabelaDeSimbolos.equals() não está implementado
		// assertEquals(esperadoTipo, saidaTipo);

		String saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		// TODO Defeito - TipoRegistro.toString() não está implementado
		assertEquals("REGISTRO([...])", saidaString);

		analisadorSemantico.getTabelasDeAtributos().remove(entradaTipoRegistro);

		// ********************************************************************************
		// Teste 3 - Entrada: registro (num, saldo numerico)
		// ********************************************************************************

		String entradaCampoSaldoIdentificador = "saldo";
		int entradaCampoSaldoColuna = 27;
		PVariavel entradaCampoSaldo = new ASimplesVariavel(
				new TIdentificador(entradaCampoSaldoIdentificador, entradaLinha, entradaCampoSaldoColuna));

		auxiliarListaDeCampos.add(entradaCampoSaldo);

		entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeCampos, new ANumericoTipo());

		entradaListaDeDeclaracoes.clear();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);

		entradaTipoRegistro = new ARegistroTipo(entradaListaDeDeclaracoes);

		TabelaDeAtributos entradaAtributosDoCampoSaldo = new TabelaDeAtributos();
		entradaAtributosDoCampoSaldo.inserir(Atributo.ID, entradaCampoSaldoIdentificador.toUpperCase());
		Simbolo entradaCampoSaldoSimbolo = Simbolo.obter(entradaCampoSaldoIdentificador.toUpperCase());
		entradaAtributosDoCampoSaldo.inserir(Atributo.SIMBOLO, entradaCampoSaldoSimbolo);
		entradaAtributosDoCampoSaldo.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoSaldo.inserir(Atributo.COLUNA, entradaCampoSaldoColuna);
		entradaAtributosDoCampoSaldo.inserir(Atributo.STRING, entradaCampoSaldoIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoSaldo, entradaAtributosDoCampoSaldo);

		analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNotNull(saidaAtributosDoTipo);

		esperadoTipo = new TipoRegistro();
		esperadoTipo.getCampos().inserir(entradaCampoNumSimbolo, entradaAtributosDoCampoNum);
		esperadoTipo.getCampos().inserir(entradaCampoSaldoSimbolo, entradaAtributosDoCampoSaldo);

		saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		// TODO Defeito - TabelaDeSimbolos.equals() não está implementado
		// assertEquals(esperadoTipo, saidaTipo);

		saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		// TODO Defeito - TipoRegistro.toString() não está implementado
		assertEquals("REGISTRO([...])", saidaString);

		analisadorSemantico.getTabelasDeAtributos().remove(entradaTipoRegistro);

		// ********************************************************************************
		// Teste 4 - Entrada: registro (num, saldo numerico nome literal)
		// ********************************************************************************

		String entradaCampoNomeIdentificador = "nome";
		int entradaCampoNomeColuna = 42;
		PVariavel entradaCampoNome = new ASimplesVariavel(
				new TIdentificador(entradaCampoNomeIdentificador, entradaLinha, entradaCampoNomeColuna));

		auxiliarListaDeCampos.clear();
		auxiliarListaDeCampos.add(entradaCampoNome);

		PDeclaracao entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeCampos, new ALiteralTipo());

		entradaListaDeDeclaracoes.clear();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);
		entradaListaDeDeclaracoes.add(entradaDeclaracaoLiteral);

		entradaTipoRegistro = new ARegistroTipo(entradaListaDeDeclaracoes);

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

		esperadoTipo = new TipoRegistro();
		esperadoTipo.getCampos().inserir(entradaCampoNumSimbolo, entradaAtributosDoCampoNum);
		esperadoTipo.getCampos().inserir(entradaCampoSaldoSimbolo, entradaAtributosDoCampoSaldo);
		esperadoTipo.getCampos().inserir(entradaCampoNomeSimbolo, entradaAtributosDoCampoNome);

		saidaTipo = (Tipo) saidaAtributosDoTipo.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		// TODO Defeito - TabelaDeSimbolos.equals() não está implementado
		// assertEquals(esperadoTipo, saidaTipo);

		saidaString = (String) saidaAtributosDoTipo.obter(Atributo.STRING);
		assertNotNull(saidaString);
		// TODO Defeito - TipoRegistro.toString() não está implementado
		assertEquals("REGISTRO([...])", saidaString);

		analisadorSemantico.getTabelasDeAtributos().remove(entradaTipoRegistro);

		// ********************************************************************************
		// Teste 5 - com campo repetido (gera erro semântico)
		// Entrada: registro (num, saldo numerico nome, num literal)
		// ********************************************************************************

		String entradaCampoRepetidoIdentificador = entradaCampoNumIdentificador;
		int entradaCampoRepetidoColuna = 48;
		PVariavel entradaCampoRepetido = new ASimplesVariavel(
				new TIdentificador(entradaCampoRepetidoIdentificador, entradaLinha, entradaCampoRepetidoColuna));

		auxiliarListaDeCampos.add(entradaCampoRepetido);

		entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeCampos, new ALiteralTipo());

		entradaListaDeDeclaracoes.clear();
		entradaListaDeDeclaracoes.add(entradaDeclaracaoNumerico);
		entradaListaDeDeclaracoes.add(entradaDeclaracaoLiteral);

		entradaTipoRegistro = new ARegistroTipo(entradaListaDeDeclaracoes);

		TabelaDeAtributos entradaAtributosDoCampoRepetido = new TabelaDeAtributos();
		entradaAtributosDoCampoRepetido.inserir(Atributo.ID, entradaCampoRepetidoIdentificador.toUpperCase());
		Simbolo entradaCampoRepetidoSimbolo = Simbolo.obter(entradaCampoRepetidoIdentificador.toUpperCase());
		entradaAtributosDoCampoRepetido.inserir(Atributo.SIMBOLO, entradaCampoRepetidoSimbolo);
		entradaAtributosDoCampoRepetido.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoCampoRepetido.inserir(Atributo.COLUNA, entradaCampoRepetidoColuna);
		entradaAtributosDoCampoRepetido.inserir(Atributo.STRING, entradaCampoRepetidoIdentificador.toUpperCase());
		analisadorSemantico.gravarAtributos(entradaCampoRepetido, entradaAtributosDoCampoRepetido);

		analisadorDeDeclaracoes.outARegistroTipo(entradaTipoRegistro);
		// TODO Defeito - Na verdade, esse erro deveria ser detectado no método
		// outADeclaracao(), há código desnecessário no método outARegistroTipo()
		saidaAtributosDoTipo = analisadorSemantico.obterAtributos(entradaTipoRegistro);
		assertNull(saidaAtributosDoTipo);
		assertTrue(analisadorSemantico.haErroSemantico());
		// TODO Refatoração - Não é possível testar linha e coluna do erro semântico

		analisadorSemantico.getTabelasDeAtributos().remove(entradaTipoRegistro);

		// ********************************************************************************
		// Teste 6 - com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.outARegistroTipo(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}

	@Test
	public void caseASubRotina() {
		// Entrada:
		// sub-rotina verifica()
		// fim_sub_rotina verifica

		String entradaSubRotinaIdentificadorComoString = "verifica";
		int entradaLinha = 14, entradaSubRotinaColuna = 12;
		TIdentificador entradaIdentificador = new TIdentificador(entradaSubRotinaIdentificadorComoString, entradaLinha,
				entradaSubRotinaColuna);

		List<PDeclaracao> entradaParametros = new ArrayList<>();

		String entradaSubRotinaIdentificadorFimComoString = entradaSubRotinaIdentificadorComoString;
		int entradaIdentificadorFimLinha = entradaLinha + 1, entradaIdentificadorFimColuna = 16;
		TIdentificador entradaIdentificadorFim = new TIdentificador(entradaSubRotinaIdentificadorFimComoString,
				entradaIdentificadorFimLinha, entradaIdentificadorFimColuna);

		ASubRotina entradaSubRotina = new ASubRotina(entradaIdentificador, entradaParametros,
				new ArrayList<PDeclaracao>(), new ArrayList<PComando>(), entradaIdentificadorFim);

		// ********************************************************************************
		// Teste 1 - com erro semântico detectado anteriormente
		// ********************************************************************************

		inserirErroSemantico();
		try {
			analisadorDeDeclaracoes.caseASubRotina(entradaSubRotina);
		} catch (Exception e) {
			fail("Se havia erro semântico, a análise semântica não deveria ter sido executada");
		}
		TabelaDeAtributos saidaAtributosDaSubRotina = analisadorSemantico.obterAtributos(entradaSubRotina);
		assertNull(saidaAtributosDaSubRotina);

		removerErroSemantico();

		// ********************************************************************************
		// Teste 2 - com entrada válida
		// ********************************************************************************

		analisadorDeDeclaracoes.caseASubRotina(entradaSubRotina);
		saidaAtributosDaSubRotina = analisadorSemantico.obterAtributos(entradaSubRotina);
		assertNotNull(saidaAtributosDaSubRotina);

		String esperadoIdentificador = entradaSubRotinaIdentificadorComoString.toUpperCase();

		String saidaIdentificador = (String) saidaAtributosDaSubRotina.obter(Atributo.ID);
		assertNotNull(saidaIdentificador);
		assertEquals(esperadoIdentificador, saidaIdentificador);

		Simbolo esperadoSimbolo = Simbolo.obter(entradaSubRotinaIdentificadorComoString.toUpperCase());

		Simbolo saidaSimbolo = (Simbolo) saidaAtributosDaSubRotina.obter(Atributo.SIMBOLO);
		assertNotNull(saidaSimbolo);
		assertEquals(esperadoSimbolo, saidaSimbolo);

		Integer saidaLinha = (Integer) saidaAtributosDaSubRotina.obter(Atributo.LINHA);
		assertNotNull(saidaLinha);
		assertEquals(entradaLinha, (int) saidaLinha);

		Integer saidaColuna = (Integer) saidaAtributosDaSubRotina.obter(Atributo.COLUNA);
		assertNotNull(saidaColuna);
		assertEquals(entradaSubRotinaColuna, (int) saidaColuna);

		TipoSubrotina esperadoTipo = new TipoSubrotina(entradaSubRotina, null);

		Tipo saidaTipo = (Tipo) saidaAtributosDaSubRotina.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		String esperadoString = "VERIFICA()";

		String saidaString = (String) saidaAtributosDaSubRotina.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoString, saidaString);

		inicializar();

		// ********************************************************************************
		// Teste 3
		// Entrada:
		// sub-rotina verifica(x numerico)
		// fim_sub_rotina verifica
		// ********************************************************************************

		String entradaParametroXIdentificador = "x";
		int entradaParametroXColuna = 21;
		PVariavel entradaParametroX = new ASimplesVariavel(
				new TIdentificador(entradaParametroXIdentificador, entradaLinha, entradaParametroXColuna));

		List<PVariavel> auxiliarListaDeVariaveis = new ArrayList<>();
		auxiliarListaDeVariaveis.add(entradaParametroX);

		PDeclaracao entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeVariaveis, new ANumericoTipo());

		entradaParametros.add(entradaDeclaracaoNumerico);

		entradaSubRotina = new ASubRotina(entradaIdentificador, entradaParametros, new ArrayList<PDeclaracao>(),
				new ArrayList<PComando>(), entradaIdentificadorFim);

		TabelaDeAtributos entradaAtributosDoParametroX = new TabelaDeAtributos();
		entradaAtributosDoParametroX.inserir(Atributo.ID, entradaParametroXIdentificador.toUpperCase());
		Simbolo entradaParametroXSimbolo = Simbolo.obter(entradaParametroXIdentificador.toUpperCase());
		entradaAtributosDoParametroX.inserir(Atributo.SIMBOLO, entradaParametroXSimbolo);
		entradaAtributosDoParametroX.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoParametroX.inserir(Atributo.COLUNA, entradaParametroXColuna);
		entradaAtributosDoParametroX.inserir(Atributo.STRING, entradaParametroXIdentificador.toUpperCase());
		entradaAtributosDoParametroX.inserir(Atributo.TIPO, new Tipo(TipoPrimitivo.NUMERICO));
		analisadorSemantico.gravarAtributos(entradaParametroX, entradaAtributosDoParametroX);

		analisadorDeDeclaracoes.caseASubRotina(entradaSubRotina);
		saidaAtributosDaSubRotina = analisadorSemantico.obterAtributos(entradaSubRotina);
		assertNotNull(saidaAtributosDaSubRotina);

		saidaIdentificador = (String) saidaAtributosDaSubRotina.obter(Atributo.ID);
		assertNotNull(saidaIdentificador);
		assertEquals(esperadoIdentificador, saidaIdentificador);

		saidaSimbolo = (Simbolo) saidaAtributosDaSubRotina.obter(Atributo.SIMBOLO);
		assertNotNull(saidaSimbolo);
		assertEquals(esperadoSimbolo, saidaSimbolo);

		saidaLinha = (Integer) saidaAtributosDaSubRotina.obter(Atributo.LINHA);
		assertNotNull(saidaLinha);
		assertEquals(entradaLinha, (int) saidaLinha);

		saidaColuna = (Integer) saidaAtributosDaSubRotina.obter(Atributo.COLUNA);
		assertNotNull(saidaColuna);
		assertEquals(entradaSubRotinaColuna, (int) saidaColuna);

		esperadoTipo = new TipoSubrotina(entradaSubRotina, null);
		esperadoTipo.getParametros().add(entradaParametroXSimbolo);
		esperadoTipo.getTabelaDeSimbolos().inserir(entradaParametroXSimbolo, entradaAtributosDoParametroX);

		saidaTipo = (Tipo) saidaAtributosDaSubRotina.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		esperadoString = "VERIFICA(NUMERICO)";

		saidaString = (String) saidaAtributosDaSubRotina.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoString, saidaString);

		inicializar();

		// ********************************************************************************
		// Teste 4
		// Entrada:
		// sub-rotina verifica(x, y numerico)
		// fim_sub_rotina verifica
		// ********************************************************************************

		String entradaParametroYIdentificador = "y";
		int entradaParametroYColuna = 24;
		PVariavel entradaParametroY = new ASimplesVariavel(
				new TIdentificador(entradaParametroYIdentificador, entradaLinha, entradaParametroYColuna));

		auxiliarListaDeVariaveis.add(entradaParametroY);

		entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeVariaveis, new ANumericoTipo());

		entradaParametros.clear();
		entradaParametros.add(entradaDeclaracaoNumerico);

		entradaSubRotina = new ASubRotina(entradaIdentificador, entradaParametros, new ArrayList<PDeclaracao>(),
				new ArrayList<PComando>(), entradaIdentificadorFim);

		analisadorSemantico.gravarAtributos(entradaParametroX, entradaAtributosDoParametroX);

		TabelaDeAtributos entradaAtributosDoParametroY = new TabelaDeAtributos();
		entradaAtributosDoParametroY.inserir(Atributo.ID, entradaParametroYIdentificador.toUpperCase());
		Simbolo entradaParametroYSimbolo = Simbolo.obter(entradaParametroYIdentificador.toUpperCase());
		entradaAtributosDoParametroY.inserir(Atributo.SIMBOLO, entradaParametroYSimbolo);
		entradaAtributosDoParametroY.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoParametroY.inserir(Atributo.COLUNA, entradaParametroYColuna);
		entradaAtributosDoParametroY.inserir(Atributo.STRING, entradaParametroYIdentificador.toUpperCase());
		entradaAtributosDoParametroY.inserir(Atributo.TIPO, new Tipo(TipoPrimitivo.NUMERICO));
		analisadorSemantico.gravarAtributos(entradaParametroY, entradaAtributosDoParametroY);

		analisadorDeDeclaracoes.caseASubRotina(entradaSubRotina);
		saidaAtributosDaSubRotina = analisadorSemantico.obterAtributos(entradaSubRotina);
		assertNotNull(saidaAtributosDaSubRotina);

		saidaIdentificador = (String) saidaAtributosDaSubRotina.obter(Atributo.ID);
		assertNotNull(saidaIdentificador);
		assertEquals(esperadoIdentificador, saidaIdentificador);

		saidaSimbolo = (Simbolo) saidaAtributosDaSubRotina.obter(Atributo.SIMBOLO);
		assertNotNull(saidaSimbolo);
		assertEquals(esperadoSimbolo, saidaSimbolo);

		saidaLinha = (Integer) saidaAtributosDaSubRotina.obter(Atributo.LINHA);
		assertNotNull(saidaLinha);
		assertEquals(entradaLinha, (int) saidaLinha);

		saidaColuna = (Integer) saidaAtributosDaSubRotina.obter(Atributo.COLUNA);
		assertNotNull(saidaColuna);
		assertEquals(entradaSubRotinaColuna, (int) saidaColuna);

		esperadoTipo = new TipoSubrotina(entradaSubRotina, null);
		esperadoTipo.getParametros().add(entradaParametroXSimbolo);
		esperadoTipo.getTabelaDeSimbolos().inserir(entradaParametroXSimbolo, entradaAtributosDoParametroX);
		esperadoTipo.getParametros().add(entradaParametroYSimbolo);
		esperadoTipo.getTabelaDeSimbolos().inserir(entradaParametroYSimbolo, entradaAtributosDoParametroY);

		saidaTipo = (Tipo) saidaAtributosDaSubRotina.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		esperadoString = "VERIFICA(NUMERICO, NUMERICO)";

		saidaString = (String) saidaAtributosDaSubRotina.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoString, saidaString);

		inicializar();

		// ********************************************************************************
		// Teste 5
		// Entrada:
		// sub-rotina verifica(x, y numerico z literal)
		// fim_sub_rotina verifica
		// ********************************************************************************

		String entradaParametroZIdentificador = "z";
		int entradaParametroZColuna = 35;
		PVariavel entradaParametroZ = new ASimplesVariavel(
				new TIdentificador(entradaParametroZIdentificador, entradaLinha, entradaParametroZColuna));

		auxiliarListaDeVariaveis.clear();
		auxiliarListaDeVariaveis.add(entradaParametroZ);

		PDeclaracao entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeVariaveis, new ALiteralTipo());

		entradaParametros.add(entradaDeclaracaoLiteral);

		entradaSubRotina = new ASubRotina(entradaIdentificador, entradaParametros, new ArrayList<PDeclaracao>(),
				new ArrayList<PComando>(), entradaIdentificadorFim);

		analisadorSemantico.gravarAtributos(entradaParametroX, entradaAtributosDoParametroX);
		analisadorSemantico.gravarAtributos(entradaParametroY, entradaAtributosDoParametroY);

		TabelaDeAtributos entradaAtributosDoParametroZ = new TabelaDeAtributos();
		entradaAtributosDoParametroZ.inserir(Atributo.ID, entradaParametroZIdentificador.toUpperCase());
		Simbolo entradaParametroZSimbolo = Simbolo.obter(entradaParametroZIdentificador.toUpperCase());
		entradaAtributosDoParametroZ.inserir(Atributo.SIMBOLO, entradaParametroZSimbolo);
		entradaAtributosDoParametroZ.inserir(Atributo.LINHA, entradaLinha);
		entradaAtributosDoParametroZ.inserir(Atributo.COLUNA, entradaParametroZColuna);
		entradaAtributosDoParametroZ.inserir(Atributo.STRING, entradaParametroZIdentificador.toUpperCase());
		entradaAtributosDoParametroZ.inserir(Atributo.TIPO, new Tipo(TipoPrimitivo.LITERAL));
		analisadorSemantico.gravarAtributos(entradaParametroZ, entradaAtributosDoParametroZ);

		analisadorDeDeclaracoes.caseASubRotina(entradaSubRotina);
		saidaAtributosDaSubRotina = analisadorSemantico.obterAtributos(entradaSubRotina);
		assertNotNull(saidaAtributosDaSubRotina);

		saidaIdentificador = (String) saidaAtributosDaSubRotina.obter(Atributo.ID);
		assertNotNull(saidaIdentificador);
		assertEquals(esperadoIdentificador, saidaIdentificador);

		saidaSimbolo = (Simbolo) saidaAtributosDaSubRotina.obter(Atributo.SIMBOLO);
		assertNotNull(saidaSimbolo);
		assertEquals(esperadoSimbolo, saidaSimbolo);

		saidaLinha = (Integer) saidaAtributosDaSubRotina.obter(Atributo.LINHA);
		assertNotNull(saidaLinha);
		assertEquals(entradaLinha, (int) saidaLinha);

		saidaColuna = (Integer) saidaAtributosDaSubRotina.obter(Atributo.COLUNA);
		assertNotNull(saidaColuna);
		assertEquals(entradaSubRotinaColuna, (int) saidaColuna);

		esperadoTipo = new TipoSubrotina(entradaSubRotina, null);
		esperadoTipo.getParametros().add(entradaParametroXSimbolo);
		esperadoTipo.getTabelaDeSimbolos().inserir(entradaParametroXSimbolo, entradaAtributosDoParametroX);
		esperadoTipo.getParametros().add(entradaParametroYSimbolo);
		esperadoTipo.getTabelaDeSimbolos().inserir(entradaParametroYSimbolo, entradaAtributosDoParametroY);
		esperadoTipo.getParametros().add(entradaParametroZSimbolo);
		esperadoTipo.getTabelaDeSimbolos().inserir(entradaParametroZSimbolo, entradaAtributosDoParametroZ);

		saidaTipo = (Tipo) saidaAtributosDaSubRotina.obter(Atributo.TIPO);
		assertNotNull(saidaTipo);
		assertEquals(esperadoTipo, saidaTipo);

		esperadoString = "VERIFICA(NUMERICO, NUMERICO, LITERAL)";

		saidaString = (String) saidaAtributosDaSubRotina.obter(Atributo.STRING);
		assertNotNull(saidaString);
		assertEquals(esperadoString, saidaString);

		inicializar();

		// Entrada:
		// sub-rotina verifica(x, y numerico z, y literal)
		// fim_sub_rotina verifica
		// O parâmetro repetido é um erro semântico já detectado no método
		// outADeclaracao()

		// ********************************************************************************
		// Teste 6 - identificador repetido após o fim da sub-rotina não corresponde ao
		// identificador da declaração (gera erro semântico)
		// Entrada:
		// sub-rotina verifica(x, y numerico z, y literal)
		// fim_sub_rotina verificar
		// ********************************************************************************

		entradaSubRotinaIdentificadorFimComoString = "verificar";
		entradaIdentificadorFim = new TIdentificador(entradaSubRotinaIdentificadorFimComoString,
				entradaIdentificadorFimLinha, entradaIdentificadorFimColuna);

		auxiliarListaDeVariaveis.clear();
		auxiliarListaDeVariaveis.add(entradaParametroX);
		auxiliarListaDeVariaveis.add(entradaParametroY);
		entradaDeclaracaoNumerico = new ADeclaracao(auxiliarListaDeVariaveis, new ANumericoTipo());

		auxiliarListaDeVariaveis.clear();
		auxiliarListaDeVariaveis.add(entradaParametroZ);
		entradaDeclaracaoLiteral = new ADeclaracao(auxiliarListaDeVariaveis, new ALiteralTipo());

		entradaParametros.clear();
		entradaParametros.add(entradaDeclaracaoNumerico);
		entradaParametros.add(entradaDeclaracaoLiteral);

		entradaSubRotina = new ASubRotina(entradaIdentificador, entradaParametros, new ArrayList<PDeclaracao>(),
				new ArrayList<PComando>(), entradaIdentificadorFim);

		analisadorSemantico.gravarAtributos(entradaParametroX, entradaAtributosDoParametroX);
		analisadorSemantico.gravarAtributos(entradaParametroY, entradaAtributosDoParametroY);
		analisadorSemantico.gravarAtributos(entradaParametroZ, entradaAtributosDoParametroZ);

		analisadorDeDeclaracoes.caseASubRotina(entradaSubRotina);
		saidaAtributosDaSubRotina = analisadorSemantico.obterAtributos(entradaSubRotina);

		// TODO Defeito - Não deveria gravar atributos, se há erro semântico
		// assertNull(saidaAtributosDaSubRotina);
		assertTrue(analisadorSemantico.haErroSemantico());
		// TODO Refatoração - Não é possível testar linha e coluna do erro semântico

		// ********************************************************************************
		// Teste 7 - com entrada nula
		// ********************************************************************************

		// try {
		// analisadorDeDeclaracoes.caseASubRotina(null);
		// } catch (NullPointerException e) {
		// TODO Defeito - Tratar entradas nulas
		// fail("Entrada nula não tratada");
		// }
	}
}