package com.company;

/**
 * Created by Helder on 02/04/2017.
 */

import java.util.Random;

public class AlgoritimoGenetico {

    private static int N = 16; //Tamanho do Tabuleiro
    private static double taxaDeCrossover = 0.9; //Probabilidade de acasalamento
    private static double taxaDeMutacao = 0.1; //Probabilidade de mutação
    private static int numeroMaximoGeracoes = 10000; //Quantidade máxima de gerações
    private static int tamanhoPopulacao = 1000; //Tamanho da população
    private static int elitismo = 1; //Define a quantidade de elitismo, 0 = sem elitismo, > 0 qtd de individuos que serão mantidos


/*
* Função para a criação da nova geração de individuos
* Caso o valor de elitismo esteja marcado como verdadeiro, o individuo com maior aptidão será mantido nessa nova geração
* A partir daí,a té que a quantidade de individuos da população esteja completa, a função selecionará dois individuos
* e realizará o acasalamento entre eles.
* Uma vez que a quantidade de individuos presentes na população tenha sido atingida, ela será ordenada utilizando a
* aptidão de cada indidivudo com parametro.
*
* */
    public Populacao novaGeracao(Populacao populacao) {

        Random r;
        Populacao novaPopulacao = new Populacao(populacao.getTamPopulacao(), false);

        if (elitismo > 0) {
            for (int i = 0; i < elitismo; i++) {
                novaPopulacao.setIndividuo(populacao.getIndividuo(i));
            }

        }

        while (novaPopulacao.getNumIndividuos() < novaPopulacao.getTamPopulacao()) {

            Individuo pais[] = new Individuo[2];
            Individuo filhos[] = new Individuo[2];

            pais[0] = selecaoTorneio(populacao);
            pais[1] = selecaoTorneio(populacao);

            r = new Random();
            if (r.nextDouble() <= taxaDeCrossover) {
                filhos = this.crossover(pais[0], pais[1]);
                filhos[0].geraAptidao();
                filhos[1].geraAptidao();
                if (r.nextDouble() < AlgoritimoGenetico.getTaxaDeMutacao()){
                    filhos[0] = this.mutarIndividuo(filhos[0]);
                }
            }
            novaPopulacao.setIndividuo(filhos[0]);
            novaPopulacao.setIndividuo(filhos[1]);
        }
        novaPopulacao.ordenaPopulacao();
        return novaPopulacao;
    }


    /*
    * Função de crossover
    * Recebe dois individuos como parametro e realiza um crossover entre eles para a geração de 2 filhos
    * O ponto de corte é um valor aleatorio, com minimo 0 e máximo igual ao tamanho do tabuleiro.
    * O resultado é um array contendo dois filhos gerados a partir dos pais passados como parametro.
    * O genes dos filhos são definidos da seguinte forma:
    * Uma vez que o ponto de corte tenha sido definido, o filho 1 receberá os genes das posições menores que o ponto de corte do pai1, o restante dos genes virão do pai2.
    * Um processo semelhante ocorrerá com o filho2. Os genes em posição abaixo do ponto de corte terão o pai2 como origem, enquanto os demais virão do pai1.
    * */
    public Individuo[] crossover(Individuo pai1, Individuo pai2) {
        Random r = new Random();
        Individuo filhos[] = new Individuo[2];

        filhos[0] = new Individuo(false);
        filhos[1] = new Individuo(false);

        int pontoCorte = r.nextInt(AlgoritimoGenetico.N);

        for (int i = 0; i < AlgoritimoGenetico.N; i++) {
            if (i < pontoCorte) {
                filhos[0].addRainha(i, pai1.getPosicoes()[i]);
                filhos[1].addRainha(i, pai2.getPosicoes()[i]);
            } else {
                filhos[0].addRainha(i, pai2.getPosicoes()[i]);
                filhos[1].addRainha(i, pai1.getPosicoes()[i]);
            }
        }
        filhos[0].geraAptidao();
        filhos[1].geraAptidao();
        return filhos;
    }


    /*
    * Função de Seleção de pais
    * Esta função usa o método de torneio para a seleção de um individuo apto a reprodução.
    * Dois individuos são selecionados aleatoriamente, depois ordenados baseado no fitness de cada um.
    * O individuo com maior fitness tem 0.9 de probabilidade de ser selecionado para acasalamento. O outro possui 0.10.
    *
    *
    *
    * */
    public Individuo selecaoTorneio(Populacao populacao) {
        Random r = new Random();
        Populacao competidores = new Populacao(2, false);

        for (int i = 0; i < 2 ; i++) {
            r = new Random();
            competidores.setIndividuo(populacao.getIndividuo(r.nextInt(populacao.getTamPopulacao())));
        }
        competidores.ordenaPopulacao();
        int pos;
        r = new Random();
        if (r.nextDouble() < 0.9) {
            pos = 0;
        } else {
            pos = 1;
        }
        return competidores.getIndividuo(pos);
    }

    /*
    * Gera mutação no individuo
    * Seleciona duas posições aleatoriamente e troca o valor delas
    *
    * */
    public Individuo mutarIndividuo(Individuo individuo){
        Individuo resultado = new Individuo(false);
        resultado = individuo;
        Random rand =  new Random();
        int coluna1 = rand.nextInt(AlgoritimoGenetico.getN());
        int coluna2 = rand.nextInt(AlgoritimoGenetico.getN());
        int aux = resultado.getPosicoes()[coluna1];
        resultado.getPosicoes()[coluna1] = resultado.getPosicoes()[coluna2];
        resultado.getPosicoes()[coluna2] = aux;
        return resultado;
    }


    public static double getTaxaDeMutacao() {
        return taxaDeMutacao;
    }



    public static int getNumeroMaximoGeracoes() {
        return numeroMaximoGeracoes;
    }


    public static int getN() {
        return N;
    }


    public static int getTamanhoPopulacao() {
        return tamanhoPopulacao;
    }



    /*
    * Função para inicialização do algoritimo genetico
    * Ela gera a população inicial e realiza o controle da obtenção do objetivo ou da quantidade de gerações criadas.
    *
    * */
    public void Iniciar(){
        int geracao = 1;
        Populacao populacao = new Populacao(getTamanhoPopulacao(), true);
        populacao.ordenaPopulacao();
        while(geracao < getNumeroMaximoGeracoes()){
            System.out.println("Geração : " + geracao);
            System.out.println("Aptidão média : " + populacao.getMediaAptidao());
            System.out.println("Aptidão Total : " + populacao.getTotalAptidao());
            System.out.println("Melhor Individuo: " + populacao.getIndividuo(0) + "\n" + "Fitness: " + populacao.getIndividuo(0).getAptidao() );
            System.out.println("Pior Individuo: " + populacao.getIndividuo(this.tamanhoPopulacao-1) + "\n" + "Fitness: " + populacao.getIndividuo(this.tamanhoPopulacao-1).getAptidao() + "\n" );
            if (populacao.getIndividuo(0).getAptidao()==0){
                System.out.println("Solução Encontrada");
                System.out.println(populacao.getIndividuo(0));
                return;
            }
            geracao++;
            populacao = this.novaGeracao(populacao);
        }

    }
}
