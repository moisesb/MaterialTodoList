package com.borges.moises.materialtodolist.data.repository.specification;

import com.borges.moises.materialtodolist.data.repository.specification.Specification;

/**
 * Created by Moisés on 12/04/2016.
 */
public interface SqlSpecification extends Specification {
    String toSqlQuery();
    String[] getSelectionArgs();

}
