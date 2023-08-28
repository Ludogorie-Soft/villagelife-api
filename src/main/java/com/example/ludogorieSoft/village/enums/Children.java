package com.example.ludogorieSoft.village.enums;

import com.example.ludogorieSoft.village.exeptions.ApiRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum Children {

    BELOW_10  ("под 10", 1),
    FROM_11_TO_20 ("11 - 20", 2),
    FROM_21_TO_50 ("21 - 50", 3),
    OVER_50 ("над 50", 4);

    private final String name;
    private final int valueAsNumber;

    public Children getEnumValue() {
        return this;
    }

    public static Children getByValueAsNumber(int valueAsNumber){
        List<Children> childrenList = List.of(values());
        Children children = null;
        for(Children childrenResult : childrenList) {
            if (childrenResult.getValueAsNumber() == valueAsNumber) {
                children = childrenResult;
            }
        }
        if (children != null){
            return children;
        }
        throw new ApiRequestException("Children not found");
    }

}
