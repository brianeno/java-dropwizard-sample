/**
 * @author Brian Enochson
 * @date 01/01/2024
 */
package com.brianeno.app.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ChargeSession {

    @NotNull
    private Integer id;

    @NotBlank
    @Length(min = 2, max = 255)
    private String make;

    @NotBlank
    @Length(min = 2, max = 255)
    private String model;

    private Integer wattHours;
}
