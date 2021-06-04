package com.example.meuprojeto.model;

public class DiaSemana {
        String id;
        String dia;

        /*Dias da semana serão definidos da seguinte forma para controle no firebase:
        0 - Domingo;
        1 - Segunda;
        2 - Terça;
        3 - Quarta;
        4 - Quinta;
        5 - Sexta;
        6 - Sábado
        */

        public DiaSemana() {
        }

    public DiaSemana(String id, String dia) {
        this.id = id;
        this.dia = dia;
    }

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDia() {
            return dia;
        }

        public void setDia(String dia) {
            this.dia = dia;
        }
}
