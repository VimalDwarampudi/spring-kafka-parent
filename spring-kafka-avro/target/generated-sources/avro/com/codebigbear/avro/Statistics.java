/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.codebigbear.avro;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class Statistics extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 2722198509992632125L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"Statistics\",\"namespace\":\"com.codebigbear.avro\",\"fields\":[{\"name\":\"count\",\"type\":\"long\",\"default\":0},{\"name\":\"last_count_time\",\"type\":{\"type\":\"long\",\"logicalType\":\"timestamp-millis\"}}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<Statistics> ENCODER =
      new BinaryMessageEncoder<Statistics>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<Statistics> DECODER =
      new BinaryMessageDecoder<Statistics>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<Statistics> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<Statistics> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<Statistics>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this Statistics to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a Statistics from a ByteBuffer. */
  public static Statistics fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

   private long count;
   private org.joda.time.DateTime last_count_time;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public Statistics() {}

  /**
   * All-args constructor.
   * @param count The new value for count
   * @param last_count_time The new value for last_count_time
   */
  public Statistics(java.lang.Long count, org.joda.time.DateTime last_count_time) {
    this.count = count;
    this.last_count_time = last_count_time;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return count;
    case 1: return last_count_time;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  protected static final org.apache.avro.data.TimeConversions.DateConversion DATE_CONVERSION = new org.apache.avro.data.TimeConversions.DateConversion();
  protected static final org.apache.avro.data.TimeConversions.TimeConversion TIME_CONVERSION = new org.apache.avro.data.TimeConversions.TimeConversion();
  protected static final org.apache.avro.data.TimeConversions.TimestampConversion TIMESTAMP_CONVERSION = new org.apache.avro.data.TimeConversions.TimestampConversion();
  protected static final org.apache.avro.Conversions.DecimalConversion DECIMAL_CONVERSION = new org.apache.avro.Conversions.DecimalConversion();

  private static final org.apache.avro.Conversion<?>[] conversions =
      new org.apache.avro.Conversion<?>[] {
      null,
      TIMESTAMP_CONVERSION,
      null
  };

  @Override
  public org.apache.avro.Conversion<?> getConversion(int field) {
    return conversions[field];
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: count = (java.lang.Long)value$; break;
    case 1: last_count_time = (org.joda.time.DateTime)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'count' field.
   * @return The value of the 'count' field.
   */
  public java.lang.Long getCount() {
    return count;
  }

  /**
   * Sets the value of the 'count' field.
   * @param value the value to set.
   */
  public void setCount(java.lang.Long value) {
    this.count = value;
  }

  /**
   * Gets the value of the 'last_count_time' field.
   * @return The value of the 'last_count_time' field.
   */
  public org.joda.time.DateTime getLastCountTime() {
    return last_count_time;
  }

  /**
   * Sets the value of the 'last_count_time' field.
   * @param value the value to set.
   */
  public void setLastCountTime(org.joda.time.DateTime value) {
    this.last_count_time = value;
  }

  /**
   * Creates a new Statistics RecordBuilder.
   * @return A new Statistics RecordBuilder
   */
  public static com.codebigbear.avro.Statistics.Builder newBuilder() {
    return new com.codebigbear.avro.Statistics.Builder();
  }

  /**
   * Creates a new Statistics RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new Statistics RecordBuilder
   */
  public static com.codebigbear.avro.Statistics.Builder newBuilder(com.codebigbear.avro.Statistics.Builder other) {
    return new com.codebigbear.avro.Statistics.Builder(other);
  }

  /**
   * Creates a new Statistics RecordBuilder by copying an existing Statistics instance.
   * @param other The existing instance to copy.
   * @return A new Statistics RecordBuilder
   */
  public static com.codebigbear.avro.Statistics.Builder newBuilder(com.codebigbear.avro.Statistics other) {
    return new com.codebigbear.avro.Statistics.Builder(other);
  }

  /**
   * RecordBuilder for Statistics instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<Statistics>
    implements org.apache.avro.data.RecordBuilder<Statistics> {

    private long count;
    private org.joda.time.DateTime last_count_time;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(com.codebigbear.avro.Statistics.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.count)) {
        this.count = data().deepCopy(fields()[0].schema(), other.count);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.last_count_time)) {
        this.last_count_time = data().deepCopy(fields()[1].schema(), other.last_count_time);
        fieldSetFlags()[1] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing Statistics instance
     * @param other The existing instance to copy.
     */
    private Builder(com.codebigbear.avro.Statistics other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.count)) {
        this.count = data().deepCopy(fields()[0].schema(), other.count);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.last_count_time)) {
        this.last_count_time = data().deepCopy(fields()[1].schema(), other.last_count_time);
        fieldSetFlags()[1] = true;
      }
    }

    /**
      * Gets the value of the 'count' field.
      * @return The value.
      */
    public java.lang.Long getCount() {
      return count;
    }

    /**
      * Sets the value of the 'count' field.
      * @param value The value of 'count'.
      * @return This builder.
      */
    public com.codebigbear.avro.Statistics.Builder setCount(long value) {
      validate(fields()[0], value);
      this.count = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'count' field has been set.
      * @return True if the 'count' field has been set, false otherwise.
      */
    public boolean hasCount() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'count' field.
      * @return This builder.
      */
    public com.codebigbear.avro.Statistics.Builder clearCount() {
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'last_count_time' field.
      * @return The value.
      */
    public org.joda.time.DateTime getLastCountTime() {
      return last_count_time;
    }

    /**
      * Sets the value of the 'last_count_time' field.
      * @param value The value of 'last_count_time'.
      * @return This builder.
      */
    public com.codebigbear.avro.Statistics.Builder setLastCountTime(org.joda.time.DateTime value) {
      validate(fields()[1], value);
      this.last_count_time = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'last_count_time' field has been set.
      * @return True if the 'last_count_time' field has been set, false otherwise.
      */
    public boolean hasLastCountTime() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'last_count_time' field.
      * @return This builder.
      */
    public com.codebigbear.avro.Statistics.Builder clearLastCountTime() {
      fieldSetFlags()[1] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Statistics build() {
      try {
        Statistics record = new Statistics();
        record.count = fieldSetFlags()[0] ? this.count : (java.lang.Long) defaultValue(fields()[0], record.getConversion(0));
        record.last_count_time = fieldSetFlags()[1] ? this.last_count_time : (org.joda.time.DateTime) defaultValue(fields()[1], record.getConversion(1));
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<Statistics>
    WRITER$ = (org.apache.avro.io.DatumWriter<Statistics>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<Statistics>
    READER$ = (org.apache.avro.io.DatumReader<Statistics>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}