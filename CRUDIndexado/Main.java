/*
Nome: Larissa Domingues Gomes
Turno: Manha
Numero de Matricula: 650525
Professor: Marcos Kutova
Questao: Main - CRUD Genérico
*/

import java.io.*;

public class Main{
    public static void main(String[] args)throws Exception{
        Cliente x = new Cliente("Joao", "joao@gmail.com", "Av. Dom Pedro 1234");
        Cliente y = new Cliente("Ana", "ana@gmail.com", "Rua Conde Dolabella 543");
        Cliente z = new Cliente("Maria", "maria@gmail.com", "Rua Ceará 423");

        try{
            //new File("arq.db").delete();
            CRUD<Cliente> c = new CRUD<>(Cliente.class.getConstructor(), "arq.db");
            
            int id1 = c.create(x);
            int id2 = c.create(y); 
            int id3 = c.create(z);

            System.out.println(c.read(id3));
            System.out.println(c.read(id1));

            System.out.println(c.read(y.chaveSecundaria()));
            System.out.println(c.read("teste"));

            y.setNome("Richard Burton Matheson");
            c.update(y);
            System.out.println(c.read(id2));
            
            x.setNome("I. Asimov");
            c.update(x);
            System.out.println(c.read(id1));

            c.delete(id3);
            Cliente l = c.read(id3);
            if(l==null)
                System.out.println("Cliente excluído");
            else
                System.out.println(l);

        } catch (Exception e) {
             e.printStackTrace();
        }


    }
}