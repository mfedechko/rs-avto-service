package com.rsavto.categories.service.write;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mfedechko
 */

@Getter
@Setter
public class WriteResults {

    private int totalParts;
    private int availableParts;
    private int notAvailableParts;
    private int errorsCount;
    private int copiesCount;

    private String availablePartsFilePath;
    private String notAvailablePartsFilePath;
    private String errorsFilePath;
    private String copiesFilePath;

}
