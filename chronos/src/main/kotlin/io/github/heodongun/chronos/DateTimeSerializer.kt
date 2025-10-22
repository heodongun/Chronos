package io.github.heodongun.chronos

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for DateTime class that uses ISO 8601 format.
 */
object DateTimeSerializer : KSerializer<DateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("DateTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: DateTime) {
        encoder.encodeString(value.toIso8601String())
    }

    override fun deserialize(decoder: Decoder): DateTime {
        return DateTime.parse(decoder.decodeString())
    }
}
