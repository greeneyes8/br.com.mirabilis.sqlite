package br.com.mirabilis.sqlite.annotation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationEntity;
import br.com.mirabilis.sqlite.annotation.model.SQLiteAnnotationField;
import br.com.mirabilis.sqlite.manager.exception.SQLiteManagerException;
import br.com.mirabilis.sqlite.manager.model.SQLiteEntity;
import br.com.mirabilis.sqlite.manager.model.SQLiteField;
import br.com.mirabilis.sqlite.manager.model.SQLiteField.SQLiteType;

/**
 * Recovery values of {@link SQLiteAnnotationEntity} and
 * {@link SQLiteAnnotationField}
 * 
 * @author Rodrigo Sim�es Rosa
 */
public class SQLiteParseAnnotation {

	public static SQLiteEntity getValuesFromAnnotation(
			Class<?> classHasAnnotation) throws SQLiteManagerException {
		SQLiteAnnotationEntity entityAnnotation = null;
		List<SQLiteField> fields = null;
		if (classHasAnnotation
				.isAnnotationPresent(SQLiteAnnotationEntity.class)) {
			entityAnnotation = classHasAnnotation
					.getAnnotation(SQLiteAnnotationEntity.class);
			
			/**
			 * Get fields of superclass SQLiteTable
			 */
			for(Field field: classHasAnnotation.getSuperclass().getDeclaredFields()){
				if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {

					/**
					 * Initialize variable
					 */
					if (fields == null) {
						fields = new ArrayList<SQLiteField>();
					}
					
					fields.add(getSQLiteField(field.getAnnotation(SQLiteAnnotationField.class)));
				}
			}
			
			for (Field field : classHasAnnotation.getDeclaredFields()) {
				if (field.isAnnotationPresent(SQLiteAnnotationField.class)) {

					/**
					 * Initialize variable
					 */
					if (fields == null) {
						fields = new ArrayList<SQLiteField>();
					}

					fields.add(getSQLiteField(field.getAnnotation(SQLiteAnnotationField.class)));
				}
			}
		} else {
			throw new SQLiteManagerException("The class : ".concat(
					classHasAnnotation.getName())
					.concat(" has not annotation!"));
		}

		SQLiteEntity entity = new SQLiteEntity(entityAnnotation.name(), fields);

		return entity;
	}
	
	/**
	 * Return {@link SQLiteField} by {@link SQLiteParseAnnotation}
	 * @param annotation
	 * @return
	 * @throws SQLiteManagerException
	 */
	public static SQLiteField getSQLiteField(SQLiteAnnotationField annotation) throws SQLiteManagerException{
		return new SQLiteField.Builder(annotation.name(),
				SQLiteType.getValue(annotation.type()))
				.autoIncrement(annotation.autoIncrement())
				.notNull(annotation.notNull())
				.primaryKey(annotation.primaryKey()).build();
	}
}