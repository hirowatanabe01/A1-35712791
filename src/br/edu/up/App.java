package br.edu.up;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) {
        String alunosCsvFile = "alunos.csv";
        String resumoCsvFile = "resumo.csv";

        List<Aluno> listaDeAlunos = lerAlunosDoArquivo(alunosCsvFile);

        if (listaDeAlunos.isEmpty()) {
            System.err.println("Lista de alunos está vazia ou houve um erro na leitura.");
            return;
        }

        int totalAlunos = listaDeAlunos.size();
        int aprovados = contarAprovados(listaDeAlunos);
        int reprovados = totalAlunos - aprovados;
        double menorNota = encontrarMenorNota(listaDeAlunos);
        double maiorNota = encontrarMaiorNota(listaDeAlunos);
        double mediaGeral = calcularMediaGeral(listaDeAlunos);

        escreverResumo(resumoCsvFile, totalAlunos, aprovados, reprovados, menorNota, maiorNota, mediaGeral);

        System.out.println("Resumo gerado com sucesso no arquivo " + resumoCsvFile);
    }

    private static List<Aluno> lerAlunosDoArquivo(String alunosCsvFile) {
        List<Aluno> listaDeAlunos = new ArrayList<>();
        try (FileReader fr = new FileReader(alunosCsvFile)) {
            BufferedReader reader = new BufferedReader(fr);
            String line;
            boolean firstLine = true; // Flag to skip the first line (header)

            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header line
                }

                String[] parts = line.split(";");
                if (parts.length != 3) {
                    System.err.println("Formato inválido da linha: " + line);
                    continue;
                }

                try {
                    int matricula = Integer.parseInt(parts[0].trim());
                    String nome = parts[1].trim();
                    String notaStr = parts[2].trim().replace(',', '.');
                    double nota = Double.parseDouble(notaStr);
                    Aluno aluno = new Aluno(matricula, nome, nota);
                    listaDeAlunos.add(aluno);
                } catch (NumberFormatException e) {
                    System.err.println("Erro ao converter número na linha: " + line);
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return listaDeAlunos;
    }

    private static int contarAprovados(List<Aluno> listaDeAlunos) {
        int count = 0;
        for (Aluno aluno : listaDeAlunos) {
            if (aluno.nota >= 6.0) {
                count++;
            }
        }
        return count;
    }

    private static double encontrarMenorNota(List<Aluno> listaDeAlunos) {
        double menorNota = Double.MAX_VALUE;
        for (Aluno aluno : listaDeAlunos) {
            menorNota = Math.min(menorNota, aluno.nota);
        }
        return menorNota;
    }

    private static double encontrarMaiorNota(List<Aluno> listaDeAlunos) {
        double maiorNota = Double.MIN_VALUE;
        for (Aluno aluno : listaDeAlunos) {
            maiorNota = Math.max(maiorNota, aluno.nota);
        }
        return maiorNota;
    }

    private static double calcularMediaGeral(List<Aluno> listaDeAlunos) {
        if (listaDeAlunos.isEmpty())
            return 0.0;
        double somaNotas = 0.0;
        for (Aluno aluno : listaDeAlunos) {
            somaNotas += aluno.nota;
        }
        return somaNotas / listaDeAlunos.size();
    }

    private static void escreverResumo(String resumoCsvFile, int totalAlunos, int aprovados, int reprovados,
            double menorNota, double maiorNota, double mediaGeral) {
        try (FileWriter fw = new FileWriter(resumoCsvFile)) {
            fw.write("Quantidade de alunos na turma: " + totalAlunos + "\n");
            fw.write("Alunos aprovados: " + aprovados + "\n");
            fw.write("Alunos reprovados: " + reprovados + "\n");
            fw.write("Menor nota da turma: " + menorNota + "\n");
            fw.write("Maior nota da turma: " + maiorNota + "\n");
            fw.write("Média geral: " + mediaGeral + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Aluno {
    int matricula;
    String nome;
    double nota;

    public Aluno(int matricula, String nome, double nota) {
        this.matricula = matricula;
        this.nome = nome;
        this.nota = nota;
    }
}
