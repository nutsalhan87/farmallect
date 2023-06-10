package ru.nutsalhan87.farmallect.repository;

import ru.nutsalhan87.farmallect.model.communication.MedicamentRequest;
import ru.nutsalhan87.farmallect.model.communication.MedicamentResponse;

public interface MedicamentDynamicRepository {
    MedicamentResponse getMedicaments(MedicamentRequest medicamentRequest);
}
