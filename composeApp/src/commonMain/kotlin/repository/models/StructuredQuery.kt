package repository.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Query(
	@SerialName("structuredQuery")
	val structuredQuery: StructuredQuery? = null,
)

@Serializable
data class StructuredQuery(

	@SerialName("select")
	val select: Select? = null,

	@SerialName("limit")
	val limit: Int? = null,

	@SerialName("orderBy")
	val orderBy: List<OrderByItem?>? = null,

	@SerialName("from")
	val from: List<FromItem?>? = null,

	@SerialName("where")
	val where: Where? = null
)

@Serializable
data class FieldsItem(

	@SerialName("fieldPath")
	val fieldPath: String? = null
)

@Serializable
data class FromItem(

	@SerialName("collectionId")
	val collectionId: String? = null
)

@Serializable
data class FieldFilter(

	@SerialName("op")
	val op: String? = null,

	@SerialName("field")
	val field: Field? = null,

	@SerialName("value")
	val value: Value? = null
)

@Serializable
data class Select(

	@SerialName("fields")
	val fields: List<FieldsItem?>? = null
)

@Serializable
data class Where(

	@SerialName("fieldFilter")
	val fieldFilter: FieldFilter? = null
)

@Serializable
data class Field(

	@SerialName("fieldPath")
	val fieldPath: String? = null
)

@Serializable
data class OrderByItem(

	@SerialName("field")
	val field: Field? = null,

	@SerialName("direction")
	val direction: String? = null
)

@Serializable
data class Value(
	@SerialName("stringValue")
	val stringValue: String? = null
)
