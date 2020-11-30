/*
Nome: Larissa Domingues Gomes
Turno: Manhã
Numero de Matrícula: 650525
Professor: Marcos Kutova
Questao: CRUD Genérico
*/

import java.lang.reflect.Constructor;
import java.io.RandomAccessFile;


class CRUD <T extends Registro>{
    private long inicio;
    private long fim; 
    private Constructor<T> constructor;
    private String nome;
    private RandomAccessFile arq;

    /* Construtor classe CRUD
     * @param Constructor<T> 
     * @param String com nome do arquivo
     */
    public CRUD(Constructor<T> constructor, String nome)throws Exception{
        this.inicio = 0;
        this.constructor = constructor;
        this.nome = nome;
        //Abrir arquivo como escrita 
        arq = new RandomAccessFile(this.nome, "rw");
        if(arq.length() == 0){
            //Escrever id invalido para sinalizar que nao ha registro
            arq.writeInt(-1);
        }
        this.fim = arq.length();       

    }

    /* Metodo create CRUD generico
     * @return int de id
     * @param Objeto que sera registrado no arquivo
     */
    public int create(T registro)throws Exception{
    
        byte[] ba = registro.toByteArray();
        
        //Escrevendo no cabecalho novo ultimo id utilizado
        arq.seek(this.inicio);
        int id = arq.readInt() + 1;
        System.out.println(id);
        arq.seek(this.inicio);
        arq.writeInt(id);

        arq.seek(this.fim);//Mover para fim do arquivo
        arq.writeUTF("1");//byte para indicar campo valido

        arq.writeInt(ba.length+4);//Escrever tamanho do registro + id
        arq.writeInt(id);//Escrever id
        arq.write(ba);//Escrever registro
        this.fim = arq.length(); 
        registro.setId(id);// escrever novo id no objeto registro

        return id;
    }

    /* Metodo read CRUD generico
     * @return objeto se encontrado
     * @param int de id do objeto procurado
     */
    public T read(int id)throws Exception{
        T retorno = null; 
        arq = new RandomAccessFile(this.nome, "rw");
        arq.seek(this.inicio);     
        int ultimoId = arq.readInt();
        boolean achou = false;

        //Teste para verificar se id procurado ja foi registrado
        if(ultimoId >= id){
            retorno = this.constructor.newInstance();
            byte[] ba;
            String valido;
            while(!achou && (arq.getFilePointer() < this.fim)){
                valido = arq.readUTF();
                ba = new byte[arq.readInt()];
                arq.read(ba);
                //System.out.println("Entrou");
                if(valido.equals("1")){
                    retorno.fromByteArray(ba);
                    //System.out.println(retorno.getId());
                    achou = (retorno.getId() == id);
                }
            }
            if(!achou){
                retorno = null;
            }
        }   

        return retorno;
    }


    /* Metodo update CRUD generico
     * @return boolean de sucesso da operacao
     * @param objeto modificado
     */
    public boolean update(T alterado)throws Exception{
        boolean retorno = false; 
        arq = new RandomAccessFile(this.nome, "rw");
        arq.seek(this.inicio);     
        int ultimoId = arq.readInt();
        boolean achou = false;
        int id = alterado.getId();

        //Teste para verificar se id procurado ja foi registrado
        if(ultimoId >= id){
            T aux = this.constructor.newInstance();
            byte[] ba;
            String valido;
            int tam = 0;
            while(!achou && (arq.getFilePointer() < this.fim)){

                valido = arq.readUTF();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                if(valido.equals("1")){
                    aux.fromByteArray(ba);
                    achou = (aux.getId() == id);
                }
                
            }
            if(achou){
                byte[] novo = alterado.toByteArray();
                if(novo.length > tam-4){
                    arq.seek(arq.getFilePointer() - tam - 7);
                    arq.writeUTF("0");//Deletar arquivo
                    
                    arq.seek(this.fim);//Mover para fim do arquivo
                    
                    arq.writeUTF("1");//byte para indicar campo valido
                    arq.writeInt(novo.length+4);//Escrever tamanho do registro + id
                    arq.writeInt(id);//Escrever id
                    arq.write(novo);
                    this.fim = arq.length(); 

                    retorno = true;
                }else{
                    arq.seek(arq.getFilePointer() - tam);
                    arq.writeInt(id);
                    arq.write(novo);
                    retorno = true;
                }
            }
        }   

        return retorno;
    }

    /* Metodo delete CRUD generico
     * @return boolean de sucesso da operacao
     * @param int id
     */
    public boolean delete(int id)throws Exception{
        boolean retorno = false; 
        arq = new RandomAccessFile(this.nome, "rw");
        arq.seek(this.inicio);     
        int ultimoId = arq.readInt();
        boolean achou = false;

        //Teste para verificar se id procurado ja foi registrado
        if(ultimoId >= id){
            T aux = this.constructor.newInstance();
            byte[] ba;
            String valido;
            int tam = 0;
            while(!achou && (arq.getFilePointer() < this.fim)){

                valido = arq.readUTF();
                tam = arq.readInt();
                ba = new byte[tam];
                arq.read(ba);
                if(valido.equals("1")){
                    aux.fromByteArray(ba);
                    achou = (aux.getId() == id);
                }
            }
            if(achou){
                arq.seek(arq.getFilePointer() - tam - 7);
                arq.writeUTF("0");//Deletar arquivo
                retorno = true;
            }
        }   

        return retorno;
    }

}