package br.com.fiap.newmottugestor.config;

import br.com.fiap.newmottugestor.Leitor.Leitor;
import br.com.fiap.newmottugestor.Leitor.LeitorRepository;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.moto.Moto;
import br.com.fiap.newmottugestor.moto.MotoRepository;
import br.com.fiap.newmottugestor.patio.Patio;
import br.com.fiap.newmottugestor.patio.PatioRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PatioSeeder {

    @Autowired
    private PatioRepository patioRepository;

    @Autowired
    private LeitorRepository leitorRepository;

    @Autowired
    private MotoRepository motoRepository;

    @PostConstruct
    public void init() {
        if (patioRepository.count() > 0) {
            return;
        }

        // Criar pátios
        Patio patio1 = Patio.builder().nome("Patio A").endereco("Rua A, 123").capacidade(100.0).build();
        Patio patio2 = Patio.builder().nome("Patio B").endereco("Rua B, 456").capacidade(50.0).build();
        Patio patio3 = Patio.builder().nome("Patio C").endereco("Rua C, 789").capacidade(200.0).build();

        List<Patio> patios = patioRepository.saveAll(List.of(patio1, patio2, patio3));

        List<Leitor> leitores = new ArrayList<>();
        List<Moto> motos = new ArrayList<>();

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
                    motos.add(Moto.builder()
                            .placa("AAA" + motoCount + "1")
                            .modelo("Modelo " + motoCount)
                            .rfid_tag("RFID" + motoCount)
                            .dataCadastro(LocalDate.now())
                            .servico("Serviço " + motoCount)
                            .leitor(leitor)
                            .build());
                    motoCount++;
                }
            }
        }

        List<Leitor> leitoresSalvos = leitorRepository.saveAll(leitores);
        motoRepository.saveAll(motos);

        System.out.println("Seeder de Pátios, Leitores e Motos finalizado!");
    }
}
