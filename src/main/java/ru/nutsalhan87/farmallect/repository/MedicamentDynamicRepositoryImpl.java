package ru.nutsalhan87.farmallect.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ru.nutsalhan87.farmallect.model.communication.MedicamentRequest;
import ru.nutsalhan87.farmallect.model.communication.MedicamentResponse;
import ru.nutsalhan87.farmallect.model.medicament.Medicament;
import ru.nutsalhan87.farmallect.model.medicament.MedicamentDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicamentDynamicRepositoryImpl implements MedicamentDynamicRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public MedicamentResponse getMedicaments(MedicamentRequest medicamentRequest) {
        Map<String, Object> queryParameters = new HashMap<>();
        StringBuilder query = new StringBuilder("select med from Medicament med where med.name like :name");
        queryParameters.put("name", "%" + medicamentRequest.getName().trim() + "%");

        if (!medicamentRequest.getCompany().isEmpty()) {
            query.append(" and med.company in :companies");
            queryParameters.put("companies", medicamentRequest.getCompany());
        }
        if (!medicamentRequest.getCountry().isEmpty()) {
            query.append(" and med.country in :countries");
            queryParameters.put("countries", medicamentRequest.getCountry());
        }
        if (!medicamentRequest.getForm().isEmpty()) {
            query.append(" and med.form in :forms");
            queryParameters.put("forms", medicamentRequest.getForm());
        }

        if (!medicamentRequest.getIngridients().isEmpty()) {
            if (medicamentRequest.isIngridientsAll()) {
                query.append(" and (select count(ing) from med.ingridients ing where ing in :ingridients) = :ingridients_req_size");
                queryParameters.put("ingridients_req_size", medicamentRequest.getIngridients().size());
            } else {
                query.append(" and (select count(ing) from med.ingridients ing where ing in :ingridients) > 0");
            }
            queryParameters.put("ingridients", medicamentRequest.getIngridients());
        }
        if (!medicamentRequest.getIndications().isEmpty()) {
            if (medicamentRequest.isIndicationsAll()) {
                query.append(" and (select count(ind) from med.indications ind where ind in :indications) = :indications_req_size");
                queryParameters.put("indications_req_size", medicamentRequest.getIndications().size());
            } else {
                query.append(" and (select count(ind) from med.indications ind where ind in :indications) > 0");
            }
            queryParameters.put("indications", medicamentRequest.getIndications());
        }
        if (!medicamentRequest.getSideEffects().isEmpty()) {
            if (medicamentRequest.isSideEffectsAll()) {
                query.append(" and (select count(sf) from med.sideEffects sf where sf in :side_effects) = :side_effects_req_size");
                queryParameters.put("side_effects_req_size", medicamentRequest.getSideEffects().size());
            } else {
                query.append(" and (select count(sf) from med.sideEffects sf where sf in :side_effects) > 0");
            }
            queryParameters.put("side_effects", medicamentRequest.getSideEffects());
        }
        if (!medicamentRequest.getContraindications().isEmpty()) {
            if (medicamentRequest.isContraindicationsAll()) {
                query.append(" and (select count(ci) from med.contraindications ci where ci in :contraindications) = :contraindications_req_size");
                queryParameters.put("contraindications_req_size", medicamentRequest.getContraindications().size());
            } else {
                query.append(" and (select count(ci) from med.contraindications ci where ci in :contraindications) > 0");
            }
            queryParameters.put("contraindications", medicamentRequest.getContraindications());
        }

        TypedQuery<Medicament> entityManagerQuery = entityManager.createQuery(query.toString(), Medicament.class);
        queryParameters.forEach(entityManagerQuery::setParameter);
        List<MedicamentDTO> medicaments = entityManagerQuery.getResultStream().map(MedicamentDTO::new).toList();

        int pages = medicaments.size() % medicamentRequest.getSize() == 0 ? medicaments.size() / medicamentRequest.getSize()
                : medicaments.size() / medicamentRequest.getSize() + 1;

        if (pages < medicamentRequest.getPage()) {
            medicaments = new ArrayList<>();
        } else if (pages == medicamentRequest.getPage()) {
            medicaments = medicaments.subList((medicamentRequest.getPage() - 1) * medicamentRequest.getSize(), medicaments.size());
        } else {
            medicaments = medicaments.subList((medicamentRequest.getPage() - 1) * medicamentRequest.getSize(),
                    medicamentRequest.getPage() * medicamentRequest.getSize());
        }

        return new MedicamentResponse(medicaments, pages);
    }
}
