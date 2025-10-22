package br.com.fiap.newmottugestor.config;

import br.com.fiap.newmottugestor.enums.TipoMovimento;
import br.com.fiap.newmottugestor.enums.TipoStatus;
import br.com.fiap.newmottugestor.oracle.model.*;
import br.com.fiap.newmottugestor.oracle.repository.*;
import br.com.fiap.newmottugestor.service.MovimentoService; // <-- Importante
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
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
    private UserRepository userRepository;

    @Autowired
    private TipoMovimentacaoRepository tipoMovimentacaoRepository;

    @Autowired
    private MovimentoService movimentoService;

    @PostConstruct
    public void init() {
        if (patioRepository.count() > 0) {
            return;
        }

        System.out.println("Iniciando Seeder: Criando dados de teste...");

        Random random = new Random();
        List<String> modelos = List.of(
                "Mottu Sport",
                "Mottu Sport ESD",
                "Mottu E (elétrica)",
                "Mottu Pop"
        );

        User userSistema = User.builder()
                .name("Usuário Sistema")
                .email("sistema@mottu.com")
                .avatarUrl("http://example.com/avatar.png")
                .build();
        userSistema = userRepository.save(userSistema);
        Long idUsuarioSistema = userSistema.getId();

        if (tipoMovimentacaoRepository.count() == 0) {
            tipoMovimentacaoRepository.saveAll(List.of(
                    new TipoMovimentacao(null, "E"),
                    new TipoMovimentacao(null, "S"),
                    new TipoMovimentacao(null, "R"),
                    new TipoMovimentacao(null, "M"),
                    new TipoMovimentacao(null, "V")
            ));
        }
        List<TipoMovimento> tiposEnum = List.of(TipoMovimento.values());

        Patio patio1 = Patio.builder().nome("Patio A").endereco("Rua A, 123").capacidade(100).build(); // Corrigido para Integer
        Patio patio2 = Patio.builder().nome("Patio B").endereco("Rua B, 456").capacidade(50).build();
        Patio patio3 = Patio.builder().nome("Patio C").endereco("Rua C, 789").capacidade(200).build();
        patioRepository.saveAll(List.of(patio1, patio2, patio3));

        List<Patio> patiosSalvos = patioRepository.findAll();

        int motoCount = 1;

        for (Patio patio : patiosSalvos) {
            for (int i = 1; i <= 5; i++) {

                Leitor leitor = Leitor.builder()
                        .nome("Leitor " + patio.getNome() + " #" + i)
                        .status(TipoStatus.ATIVO)
                        .patio(patio)
                        .build();
                leitor = leitorRepository.save(leitor);

                for (int j = 1; j <= 5; j++) {
                    String placaGerada = "AAA" + (motoCount % 10) + "B" + String.format("%02d", motoCount % 100);
                    Moto moto = Moto.builder()
                            .placa(placaGerada)
                            .modelo(modelos.get(random.nextInt(modelos.size())))
                            .rfid_tag("RFID" + motoCount)
                            .dataCadastro(LocalDate.now().minusDays(random.nextInt(100)))
                            .servico("Serviço " + motoCount)
                            .leitor(leitor)
                            .build();
                    moto = motoRepository.save(moto);

                    TipoMovimento tipoAleatorio = tiposEnum.get(random.nextInt(tiposEnum.size()));

                    movimentoService.registrarNovaMovimentacao(
                            moto.getIdMoto(),
                            idUsuarioSistema,
                            patio.getIdPatio(),
                            leitor.getIdLeitor(),
                            tipoAleatorio
                    );
                    if (tipoAleatorio == TipoMovimento.SAIDA || tipoAleatorio == TipoMovimento.MANUTENCAO || tipoAleatorio == TipoMovimento.VISTORIA) {
                        moto.setLeitor(null);
                        motoRepository.save(moto); // Salva a atualização
                    }

                    motoCount++;
                }
            }
        }

        System.out.println("Seeder de Pátios, Leitores e Motos finalizado!");
    }
}