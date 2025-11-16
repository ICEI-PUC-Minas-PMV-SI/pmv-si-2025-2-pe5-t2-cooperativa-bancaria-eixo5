package com.br.pucBank.utils

interface Mapper <fromObject, toObject> {
    fun toObject(
        fromObject: fromObject
    ): toObject

    fun toObjectList(
        fromObjects: List<fromObject>
    ) = fromObjects.map { fromObject ->
        toObject(fromObject)
    }
}

interface ReversedMapper <fromObject, toObject> {
    fun toReversedObject(
        fromObject: fromObject
    ): toObject

    fun toReversedObjectList(
        fromObjects: List<fromObject>
    ) = fromObjects.map { fromObject ->
        toReversedObject(fromObject)
    }
}