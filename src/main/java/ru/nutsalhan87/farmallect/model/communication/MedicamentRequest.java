package ru.nutsalhan87.farmallect.model.communication;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MedicamentRequest {
    private String name;
    private List<String> company;
    private List<String> country;
    private List<String> form;
    private List<String> ingridients;
    private boolean ingridientsAll;
    private List<String> indications;
    private boolean indicationsAll;
    private List<String> sideEffects;
    private boolean sideEffectsAll;
    private List<String> contraindications;
    private boolean contraindicationsAll;
    private int page;
    private int size;
}
