package com.poly.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReCapchaResponse {
	private boolean success;
	private String challenge_ts;
	private String hostName;
}
