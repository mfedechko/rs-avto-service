package com.rsavto.categories.docs.response;

import com.rsavto.categories.data.CategoryType;
import lombok.Getter;
import lombok.Setter;

/**
 * @author mfedechko
 */
@Getter
@Setter
public class ProcessResponse {

    private final CategoryType categoryType;
    private int totalRecordsProcessed;
    private int totalErrors;


    public ProcessResponse(final CategoryType categoryType) {
        this.categoryType = categoryType;
    }
}
