package com.company;

/**
 * Created by Helder on 02/04/2017.
 */


import java.util.Random;


//Individuo representa uma possivel solução para o problema
public class Individuo implements Comparable<Individuo> {

    private double aptidao;
    private int[] posicoes;


    public Individuo(boolean rainhasAleatorias) {
        posicoes = new int[AlgoritimoGenetico.getN()];


        for (int i = 0; i < posicoes.length; i++) {
            posicoes[i] = -1;
        }

        for (int i = 0; i < posicoes.length; i++) {
            if (rainhasAleatorias) {
                posicoes[i] = this.gerarValorAleatorio();

            }
        }

        if (rainhasAleatorias) {
            geraAptidao();
        }
    }

    //Gera valores aletorios
    public int gerarValorAleatorio() {
        int y;
        Random r;
        boolean encontrou;
        do {
            r = new Random();
            y = r.nextInt(AlgoritimoGenetico.getN());
            encontrou = false;

            for (int i = 0; i < AlgoritimoGenetico.getN(); i++) {
                if (posicoes[i] == y) {
                    encontrou = true;
                    break;
                }
            }

        } while (encontrou);

        return y;
    }

    //Gera a aptidão baseado no número de colisões
    public void geraAptidao() {
        int repeticoes = 0;
        for (int j = 0; j < AlgoritimoGenetico.getN() ; j++) {
            for (int i = j+1; i < AlgoritimoGenetico.getN(); i++) {

                repeticoes += atacar(j, posicoes[j], i, posicoes[i]);

            }
        }
        this.aptidao = repeticoes;
    }


    //adiciona uma rainha no tabuleiro
    public void addRainha(int x, int y) {
       posicoes[x] = y;
    }



    public double getAptidao() {
        return aptidao;
    }



    public int[] getPosicoes() {
        return posicoes;
    }

    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < AlgoritimoGenetico.getN(); i++) {
            s += "[" + i + "," + posicoes[i] + "] ";
        }
        return s;
    }


    public int atacar(int x1, int y1, int x2, int y2) {
        if (y1 == y2) {
            return 2;
        }
        if (x1 == x2) {
            return 2;
        }
        if (x1 - y1 == x2 - y2 || x1 + y1 == x2 + y2) {
            return 1;
        }
        return 0;

    }


    @Override
    public int compareTo(Individuo o) {
        return (int)(this.getAptidao() - o.getAptidao());
    }
}