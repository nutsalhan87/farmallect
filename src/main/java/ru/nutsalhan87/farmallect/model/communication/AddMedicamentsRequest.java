package ru.nutsalhan87.farmallect.model.communication;

import lombok.Getter;
import ru.nutsalhan87.farmallect.model.medicament.MedicamentDTO;

import java.util.ArrayList;

@Getter
public class AddMedicamentsRequest {
    private final ArrayList<MedicamentDTO> medicaments = new ArrayList<>();
    private String token;
}
