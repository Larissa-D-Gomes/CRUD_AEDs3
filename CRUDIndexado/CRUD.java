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
    private HashExtensivel he;
    private ArvoreBMais_String_Int arvB;

    /* Construtor classe CRUD
     * @param Constructor<T> 
     * @param String com nome do arquivo
     */
    public CRUD(Constructor<T> constructor, String nome)throws Exception{
        this.inicio = 0;
        this.constructor = constructor;
        this.nome = nome;
        he = new HashExtensivel(100, "hash.db", "aux.db" );
        arvB = new ArvoreBMais_String_Int(100,"arv.db");
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
        arq.seek(this.inicio);
        arq.writeInt(id);

        arq.seek(this.fim);//Mover para fim do arquivo
        this.he.create(id, arq.getFilePointer());
        this.arvB.create(registro.chaveSecundaria(), id);
        arq.writeUTF("1");//byte para indicar campo valido

        arq.writeInt(ba.length+4);//Escrever tamanho do registro + id
        arq.writeInt(id);//Escrever id
        arq.write(ba);//Escrever registro
        this.fim = arq.length(); 
        registro.setId(id);

        return id;
    }

    /* Metodo read CRUD generico
     * @return objeto se encontrado
     * @param int de id do objeto procurado
     */
    public T read(int id)throws Exception{
        T retorno = null; 
        
        long pos = he.read(id);
        if(pos > 0){
            arq.seek(pos);

            if(arq.readUTF().equals("1")){
                retorno = constructor.newInstance();
                byte[] ba = new byte[arq.readInt()];
                arq.read(ba);
                retorno.fromByteArray(ba);
            }
        }
        return retorno;
    }

    /* Metodo read CRUD generico
     * @return objeto se encontrado
     * @param string chave secundaria do objeto procurado
     */
    public T read(String chave)throws Exception{
        T retorno = null; 
        
        int id = arvB.read(chave);
        
        if(id > -1){ 
            long pos = he.read(id);
            if(pos > 0){
                arq.seek(pos);

                if(arq.readUTF().equals("1")){
                    retorno = constructor.newInstance();
                    byte[] ba = new byte[arq.readInt()];
                    arq.read(ba);
                    retorno.fromByteArray(ba);
                }
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
        arq.seek(this.inicio);     
        int id = alterado.getId();
        T aux = constructor.newInstance();
        long pos = he.read(id);
        if(pos > 0){
            arq.seek(pos);

            if(arq.readUTF().equals("1")){
                byte[] ba = new byte[arq.readInt()];
                arq.read(ba);
                aux.fromByteArray(ba);

                byte[] novo = alterado.toByteArray();

                if(novo.length > ba.length-4){
                    arq.seek(arq.getFilePointer() - ba.length - 7);
                    arq.writeUTF("0");//Deletar arquivo
                    
                    arq.seek(this.fim);//Mover para fim do arquivo

                    if(!alterado.chaveSecundaria().equals(aux.chaveSecundaria()))
                        arvB.update(alterado.chaveSecundaria(), id);

                    he.update(id, arq.getFilePointer());
                    
                    arq.writeUTF("1");//byte para indicar campo valido
                    arq.writeInt(novo.length+4);//Escrever tamanho do registro + id
                    arq.writeInt(id);//Escrever id
                    arq.write(novo);
                    this.fim = arq.length(); 
                    retorno = true;
                }else{
                    arq.seek(arq.getFilePointer() - ba.length);
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
        arq.seek(this.inicio);     
        long pos = he.read(id);

        if(pos > -1){
            arq.seek(pos);

            if(arq.readUTF().equals("1")){
                T aux = this.constructor.newInstance();
                byte[] ba = new byte[arq.readInt()];
                aux.fromByteArray(ba);
                he.delete(aux.getId());
                arvB.delete(aux.chaveSecundaria());

                arq.seek(arq.getFilePointer() - 7);
                arq.writeUTF("0");//Deletar arquivo
                retorno = true;
            }

        }
   

        return retorno;
    }

}