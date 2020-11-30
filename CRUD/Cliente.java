/*
Nome: Larissa Domingues Gomes
Turno: Manha
Numero de Matricula: 650525
Professor: Marcos Kutova
Questao: Cliente - CRUD Genérico
*/

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

class Cliente implements Registro{
    private String nome;
    private String email;
    private String endereco;
    protected int id;

    /*
     * Construtor vazio de Cliente
     */
    public Cliente(){
        this.nome = "";
        this.email = "";
        this.endereco = "";
        this.id = -1;
    }

    /**
	 * Construtor da clases
	 * @param nome nome do cliente
     * @param email email do cliente
     * @param endereco endereco do cliente
	 */
    public Cliente(String nome, String email, String endereco){
        this(nome, email, endereco, 0);
    }

    /**
	 * Construtor da clases
	 * @param nome nome do cliente
     * @param email email do cliente
     * @param endereco endereco do cliente
     * @param id temporario (lixo)
	 */
    public Cliente(String nome, String email, String endereco, int id){
        this.nome = nome;
        this.email = email;
        this.endereco = endereco;
        this.id = id;
    }
    
	/**
	 * Metodo para retornar nome de cliente
	 * @return String de nome
	 */
    public String getNome(){
        return this.nome;
    }

	/**
	 * Metodo para retornar email de cliente
	 * @return String de email
	 */
    public String getEmail(){
        return this.email;
    }

    /**
	 * Metodo para retornar endereco de cliente
	 * @return String de endereco
	 */
    public String getEndereco(){
        return this.endereco;
    }

    /**
	 * Metodo para retornar id de cliente
	 * @return Int de id
	 */
    public int getId(){
        return this.id;
    }

    /**
	 * Metodo para alterar nome de cliente
	 * @param String de nome
	 */
    public void setNome(String nome){
        this.nome = nome;
    }

    /**
	 * Metodo para alterar email de cliente
	 * @param String de email
	 */
    public void setEmail(String email){
        this.email = email;
    }

    /**
	 * Metodo para alterar id de cliente
	 * @param int de id
	 */
    public void setId(int id){
        this.id = id;
    }


	/**
	 * Metodo para concatenar atributos da classe em uma String
	 * @return String de atributos
	 */
    public String toString(){   
        return "Nome: " + this.nome + "\nEmail: " + this.email +
                "\nEndereço: " + this.endereco + "\nId: " + this.id + "\n";  
    }

    /**
	 * Metodo para tranformar dados da classe em array de Bytes 
	 * @return Byte Array de atributos da classe
	 */
    public byte[] toByteArray() throws IOException{
        ByteArrayOutputStream ba = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(ba);

        dos.writeUTF(this.nome);
        dos.writeUTF(this.email);
        dos.writeUTF(this.endereco);
       // System.out.println(ba);
    
        return ba.toByteArray();
    }

    /**
	 * Metodo para criar cliente com byte array
	 * @param byte array com dados dos campos de Cliente
	 */
    public void fromByteArray(byte[] ba)throws IOException{
        ByteArrayInputStream bai = new ByteArrayInputStream(ba);
        DataInputStream dai = new DataInputStream(bai);

        this.id = dai.readInt();
        this.nome = dai.readUTF();
        this.email = dai.readUTF();
        this.endereco = dai.readUTF();
    }

    /**
	 * Metodo para clonar classe
	 * @return Novo Cliente identico ao que chamou ao metodo
	 */
    public Cliente clone(){
        return new Cliente(this.nome, this.email, this.endereco, this.id);
    }
}