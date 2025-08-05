package com.example.springai_chat.entity.po;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author author
 * @since 2025-08-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private String city;

}
