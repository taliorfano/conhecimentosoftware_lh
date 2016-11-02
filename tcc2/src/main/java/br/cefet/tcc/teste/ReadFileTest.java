package br.cefet.tcc.teste;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ReadFileTest {
	public static void main( String[] args ) throws IOException
    {
		readCsvFile();
    }
	public static void readCsvFile() {
		
		// A estrutura try-catch é usada pois o objeto BufferedWriter exige que
		// as
		// excessões sejam tratadas
		try {
			// Criação de um buffer para a ler de uma stream
			BufferedReader StrR = new BufferedReader(new FileReader("/home/talita/AutoriaCSV/wikidata1.csv"));
			//BufferedReader StrR = new BufferedReader(new FileReader("/home/talita/AutoriaCSV/ownership_wikidata.csv"));
			
			
			String Str;
			String[] TableLine;
			
			// Essa estrutura do looping while é clássica para ler cada linha
			// do arquivo
			while ((Str = StrR.readLine()) != null) {
				// Aqui usamos o método split que divide a linha lida em um
				// array de String
				// passando como parametro o divisor ";".
				TableLine = Str.split(";");

				// O foreach é usadao para imprimir cada célula do array de
				// String.
				for (String cell : TableLine) {
					System.out.print(cell + " ");
				}
				System.out.println("\n");
			}
			// Fechamos o buffer
			StrR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	

	public static void createCsvFile() {
		try {

			// Criação de um buffer para a escrita em uma stream
			BufferedWriter StrW = new BufferedWriter(new FileWriter("/home/talita/AutoriaCSV/tabela.csv"));

			// Escrita dos dados da tabela
			StrW.write("Nome;Telefone;Idade\n");
			StrW.write("Juliana;6783-8490;23");
			StrW.write("Tatiana;6743-7480;45\n");
			StrW.write("Janice;6909-9380;21");

			// Fechamos o buffer
			StrW.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void readCsvFile2() {

		// A estrutura try-catch é usada pois o objeto BufferedWriter exige que
		// as
		// excessões sejam tratadas
		try {

			// Criação de um buffer para a ler de uma stream
			BufferedReader StrR = new BufferedReader(new FileReader("/home/talita/tabela.csv"));

			String Str;
			String[] TableLine;

			// Essa estrutura do looping while é clássica para ler cada linha
			// do arquivo
			while ((Str = StrR.readLine()) != null) {
				// Aqui usamos o método split que divide a linha lida em um
				// array de String
				// passando como parametro o divisor ";".
				TableLine = Str.split(";");

				// O foreach é usadao para imprimir cada célula do array de
				// String.
				for (String cell : TableLine) {
					System.out.print(cell + " ");
				}
				System.out.println("\n");
			}
			// Fechamos o buffer
			StrR.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
