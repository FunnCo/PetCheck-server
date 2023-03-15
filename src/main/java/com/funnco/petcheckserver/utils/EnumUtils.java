package com.funnco.petcheckserver.utils;

import com.funnco.petcheckserver.entity.GenderEnum;
import com.funnco.petcheckserver.entity.LifeStatusEnum;

public class EnumUtils {
    public static boolean doesLifeStatusEnumContainValue(String value){
        if (value == null){
            return true;
        }
        for(LifeStatusEnum status : LifeStatusEnum.values()){
            if(value.equals(status.getValue())){
                return true;
            }
        }
        return false;
    }

    public static boolean doesGenderEnumContainValue(String value){
        if (value == null){
            return true;
        }
        for(GenderEnum gender : GenderEnum.values()){
            if(value.equals(gender.getValue())){
                return true;
            }
        }
        return false;
    }
}
