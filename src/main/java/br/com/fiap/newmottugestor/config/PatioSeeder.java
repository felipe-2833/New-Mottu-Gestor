package br.com.fiap.newmottugestor.config;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorRepository;
import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.moto.MotoRepository;
import br.com.fiap.newmottugestor.movimento.Movimento;
import br.com.fiap.newmottugestor.movimento.MovimentoRepository;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class PatioSeeder {

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private LeitorRepository leitorRepository;

    @Autowired
    private MotoRepository motoRepository;

    @Autowired
    private MovimentoRepository movimentoRepository;

    @PostConstruct
    public void init() {
        if (patioRepository.count() > 0) {
            return;
        }

        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );
        Random random = new Random();

        // Criar pátios
        Patio patio1 = Patio.builder().nome("Patio A").endereco("Rua A, 123").capacidade(100.0).build();
        Patio patio2 = Patio.builder().nome("Patio B").endereco("Rua B, 456").capacidade(50.0).build();
        Patio patio3 = Patio.builder().nome("Patio C").endereco("Rua C, 789").capacidade(200.0).build();

        List<Patio> patios = patioRepository.saveAll(List.of(patio1, patio2, patio3));

        List<Leitor> leitores = new ArrayList<>();
        List<Moto> motos = new ArrayList<>();
        List<Movimento> movimentos = new ArrayList<>();

        int motoCount = 1;

        // Criar 5 leitores para cada pátio
        for (Patio patio : patios) {
            for (int i = 1; i <= 5; i++) {
                Leitor leitor = Leitor.builder()
                        .nome("Leitor " + patio.getNome() + " #" + i)
                        .status(TipoStatus.ATIVO)
                        .patio(patio)
                        .build();
                leitores.add(leitor);

                // Criar 5 motos para cada leitor
                for (int j = 1; j <= 5; j++) {
                    String placaGerada = "AAA" + (motoCount % 10) + "B" + String.format("%02d", motoCount % 100);

                    Moto moto = Moto.builder()
                            .placa(placaGerada)
                            .modelo(modelos.get(random.nextInt(modelos.size())))
                            .rfid_tag("RFID" + motoCount)
                            .dataCadastro(LocalDate.now())
                            .servico("Serviço " + motoCount)
                            .leitor(leitor) // atribui inicialmente
                            .build();
                    motos.add(moto);

                    // Gerar um tipo aleatório de movimento
                    TipoMovimento tipo = TipoMovimento.values()[new Random().nextInt(TipoMovimento.values().length)];

                    // Criar a movimentação
                    Movimento movimento = Movimento.builder()
                            .dataEvento(LocalDate.now().minusDays(new Random().nextInt(30))) // data aleatória nos últimos 30 dias
                            .patio(leitor.getPatio())
                            .leitor(moto.getLeitor()) // pode ser null se for saída
                            .moto(moto)
                            .tipoMovimento(tipo)
                            .build();

                    movimentos.add(movimento);

                    // Se o movimento for de saída, manutenção ou vistoria -> remove leitor
                    if (tipo == TipoMovimento.SAIDA || tipo == TipoMovimento.MANUTENCAO || tipo == TipoMovimento.VISTORIA) {
                        moto.setLeitor(null);
                    }

                    motoCount++;
                }
            }
        }

        List<Leitor> leitoresSalvos = leitorRepository.saveAll(leitores);
        motoRepository.saveAll(motos);
        movimentoRepository.saveAll(movimentos);

        System.out.println("Seeder de Pátios, Leitores e Motos finalizado!");
    }
}
