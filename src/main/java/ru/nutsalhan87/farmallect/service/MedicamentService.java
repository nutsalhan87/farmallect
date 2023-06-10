package ru.nutsalhan87.farmallect.service;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.nutsalhan87.farmallect.model.communication.MedicamentResponse;
import ru.nutsalhan87.farmallect.model.medicament.Medicament;
import ru.nutsalhan87.farmallect.model.medicament.MedicamentDTO;
import ru.nutsalhan87.farmallect.model.communication.MedicamentRequest;
import ru.nutsalhan87.farmallect.model.properties.Ingridient;
import ru.nutsalhan87.farmallect.repository.MedicamentRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class MedicamentService {
    private final MedicamentRepository medicamentRepository;
    private final IngridientService ingridientService;
    private final IndicationService indicationService;
    private final SideEffectService sideEffectService;
    private final ContraindicationService contraindicationService;

    @Autowired
    public MedicamentService(MedicamentRepository medicamentRepository,
                             IngridientService ingridientService, IndicationService indicationService,
                             SideEffectService sideEffectService, ContraindicationService contraindicationService) {
        this.medicamentRepository = medicamentRepository;
        this.ingridientService = ingridientService;
        this.indicationService = indicationService;
        this.sideEffectService = sideEffectService;
        this.contraindicationService = contraindicationService;
    }

    @Transactional
    public MedicamentDTO getMedicament(Long id) {
        return new MedicamentDTO(medicamentRepository.findById(id).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Transactional
    public MedicamentResponse getMedicaments(MedicamentRequest medicamentRequest) {
        return medicamentRepository.getMedicaments(medicamentRequest);
    }

    @Transactional
    public void addMedicaments(List<MedicamentDTO> medicamentDTOS) {
        medicamentDTOS.forEach(med -> {
            Medicament medicament = new Medicament(med,
                    ingridientService, indicationService, sideEffectService, contraindicationService);
            medicamentRepository.save(medicament);
            ingridientService.addIngridients(medicament.getIngridients());
            indicationService.addIndications(medicament.getIndications());
            sideEffectService.addSideEffects(medicament.getSideEffects());
            contraindicationService.addContraindications(medicament.getContraindications());
        });
    }

    @Transactional
    public List<String> getCompanies() {
        return medicamentRepository.getCompanies();
    }

    @Transactional
    public List<String> getCountries() {
        return medicamentRepository.getCountries();
    }

    @Transactional
    public List<String> getForms() {
        return medicamentRepository.getForms();
    }
}
