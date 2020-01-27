package be.hehehe.lapinstance.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

}
