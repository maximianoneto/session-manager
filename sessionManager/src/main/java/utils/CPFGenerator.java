package utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.DecimalFormat;

public class CPFGenerator {
    public static String gerarCpfValido() {
        String cpfParcial = RandomStringUtils.randomNumeric(9);

        int primeiroDigito = calcularDigito(cpfParcial, 10);
        int segundoDigito = calcularDigito(cpfParcial + primeiroDigito, 11);

        String cpfGerado = cpfParcial + primeiroDigito + segundoDigito;
        return cpfGerado;
    }

    public static int calcularDigito(String cpfParcial, int pesoMaximo) {
        int soma = 0;
        int peso = pesoMaximo;

        for (char digito : cpfParcial.toCharArray()) {
            soma += Character.getNumericValue(digito) * peso;
            peso--;
        }

        int resto = (soma * 10) % 11;
        return (resto == 10) ? 0 : resto;
    }

    public static String formatarCpf(String cpf) {
        DecimalFormat formatador = new DecimalFormat("00000000000");
        String cpfFormatado = formatador.format(Long.parseLong(cpf));
        return cpfFormatado.replaceFirst("(\\d{3})(\\d{3})(\\d{3})", "$1.$2.$3-");
    }
}
