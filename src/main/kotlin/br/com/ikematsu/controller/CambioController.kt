package br.com.ikematsu.controller

import br.com.ikematsu.model.Cambio
import br.com.ikematsu.repository.CambioRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal
import java.math.RoundingMode

@RestController
@RequestMapping("cambio-service")
class CambioController {

    @Autowired
    private lateinit var environment: Environment

    @Autowired
    private lateinit var repository: CambioRepository

    @GetMapping(value = ["/{amount}/{from}/{to}"])
    fun getCambio(
        @PathVariable("amount") amount: BigDecimal,
        @PathVariable("from") from: String,
        @PathVariable("to") to: String
    ) : Cambio {

        // se o retorno da função for nulo, ele retorna o throw
        val cambio = repository.findByFromAndTo(from, to) ?: throw RuntimeException("Currency Unsupported")
        val port = environment.getProperty("local.server.port")

        val conversionFactor = cambio.conversionFactor
        val convertedValue = conversionFactor.multiply(amount)

        cambio.convertedValue = convertedValue.setScale(2, RoundingMode.CEILING)
        cambio.environment = port

        return cambio
    }
}