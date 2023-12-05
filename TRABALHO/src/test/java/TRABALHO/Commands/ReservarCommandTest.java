package TRABALHO.Commands;

import org.junit.Assert;
import org.junit.Test;

import TRABALHO.BaseTest;

public class ReservarCommandTest extends BaseTest {
    @Test
    public void executeTest() {

        String codigoAlunoPosGrad = String.valueOf(alunoPosGrad.getCodigo());
        String codigoExemplar1 = String.valueOf(exemplar1.getCodigo());
        String codigoExemplar2 = String.valueOf(exemplar2.getCodigo());

        biblioteca.executeCommand("res", codigoAlunoPosGrad, codigoExemplar1);
        biblioteca.executeCommand("res", codigoAlunoPosGrad, codigoExemplar2);
        biblioteca.executeCommand("res", codigoAlunoPosGrad, "100");

        Assert.assertEquals(3, alunoPosGrad.obterReservasAtivas().size());
        Assert.assertEquals(exemplar1, alunoPosGrad.obterReservasAtivas().get(0).getLivro());
        Assert.assertEquals(exemplar2, alunoPosGrad.obterReservasAtivas().get(1).getLivro());
        Assert.assertEquals(100, alunoPosGrad.obterReservasAtivas().get(2).getLivro().getCodigo());

        biblioteca.executeCommand("res", codigoAlunoPosGrad, "101");
        biblioteca.executeCommand("res", codigoAlunoPosGrad, "200");
        biblioteca.executeCommand("res", codigoAlunoPosGrad, "201");
        biblioteca.executeCommand("res", codigoAlunoPosGrad, "400");
        biblioteca.executeCommand("res", codigoAlunoPosGrad, "500");
        biblioteca.executeCommand("res", codigoAlunoPosGrad, "600");

        Assert.assertEquals(alunoPosGrad.maxReservas(), alunoPosGrad.obterReservasAtivas().size());
        Assert.assertTrue(alunoPosGrad.atingiuLimiteMaximoDeReservas());
    }

    @Test
    public void naoConsegueEmprestarLivrosReservadosParaQuemNaoTemReservaENaoEhProfessorTest() {
        String codigoAlunoPosGrad = String.valueOf(alunoPosGrad.getCodigo());
        String codigoExemplar1 = String.valueOf(exemplar1.getCodigo());
        String codigoExemplar2 = String.valueOf(exemplar2.getCodigo());

        biblioteca.executeCommand("res", codigoAlunoPosGrad, codigoExemplar1);
        biblioteca.executeCommand("res", codigoAlunoPosGrad, codigoExemplar2);

        Assert.assertEquals(2, alunoPosGrad.obterReservasAtivas().size());
        Assert.assertEquals(exemplar1, alunoPosGrad.obterReservasAtivas().get(0).getLivro());
        Assert.assertEquals(exemplar2, alunoPosGrad.obterReservasAtivas().get(1).getLivro());

        biblioteca.executeCommand("emp", String.valueOf(alunoGrad.getCodigo()), codigoExemplar1);
        biblioteca.executeCommand("emp", String.valueOf(alunoGrad.getCodigo()), codigoExemplar2);

        Assert.assertEquals(2, alunoPosGrad.obterReservasAtivas().size());
        Assert.assertEquals(exemplar1, alunoPosGrad.obterReservasAtivas().get(0).getLivro());
        Assert.assertEquals(exemplar2, alunoPosGrad.obterReservasAtivas().get(1).getLivro());

        Assert.assertEquals(0, alunoGrad.obterEmprestimos(false).size());
        Assert.assertTrue(exemplar1.isDisponivel());
        Assert.assertTrue(exemplar2.isDisponivel());

        biblioteca.executeCommand("emp", codigoAlunoPosGrad, codigoExemplar1);
        biblioteca.executeCommand("emp", codigoAlunoPosGrad, codigoExemplar2);

        Assert.assertEquals(0, alunoPosGrad.obterReservasAtivas().size());
        Assert.assertEquals(2, alunoPosGrad.obterEmprestimos(true).size());
        Assert.assertEquals(exemplar1, alunoPosGrad.obterEmprestimos(false).get(0).getExemplar());
        Assert.assertEquals(exemplar2, alunoPosGrad.obterEmprestimos(false).get(1).getExemplar());
        Assert.assertFalse(exemplar1.isDisponivel());
        Assert.assertFalse(exemplar2.isDisponivel());

        biblioteca.executeCommand("dev", codigoAlunoPosGrad, codigoExemplar1);
        biblioteca.executeCommand("dev", codigoAlunoPosGrad, codigoExemplar2);

        Assert.assertEquals(0, alunoPosGrad.obterReservasAtivas().size());
        Assert.assertEquals(0, alunoPosGrad.obterEmprestimos(true).size());
        Assert.assertEquals(2, alunoPosGrad.obterEmprestimos(false).size());
        Assert.assertTrue(exemplar1.isDisponivel());
        Assert.assertTrue(exemplar2.isDisponivel());

        biblioteca.executeCommand("res", codigoAlunoPosGrad, codigoExemplar1);
        Assert.assertEquals(1, alunoPosGrad.obterReservasAtivas().size());

        biblioteca.executeCommand("emp", String.valueOf(professor.getCodigo()), codigoExemplar1);
        Assert.assertEquals(1, alunoPosGrad.obterReservasAtivas().size());
        Assert.assertEquals(1, professor.obterEmprestimos(true).size());
        Assert.assertEquals(exemplar1, professor.obterEmprestimos(true).get(0).getExemplar());
        Assert.assertFalse(exemplar1.isDisponivel());
    }
}
