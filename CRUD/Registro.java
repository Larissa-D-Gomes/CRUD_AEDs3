/*
Nome: Larissa Domingues Gomes
Turno: Manha
Numero de Matricula: 650525
Professor: Marcos Kutova
Questao: interface Registro - CRUD Genérico
*/

import java.io.IOException;

public interface Registro{
    public int getId();
    public void setId(int n);
    public byte[] toByteArray() throws IOException;
    public void fromByteArray( byte[] ba ) throws IOException;
}