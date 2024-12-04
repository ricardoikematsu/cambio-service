package br.com.ikematsu.repository

import br.com.ikematsu.model.Cambio
import org.springframework.data.jpa.repository.JpaRepository

interface CambioRepository : JpaRepository<Cambio, Long?>{

    fun findByFromAndTo(from: String, to: String): Cambio?
}