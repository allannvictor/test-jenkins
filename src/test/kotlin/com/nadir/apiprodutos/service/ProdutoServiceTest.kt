package com.nadir.apiprodutos.service

import com.nadir.apiprodutos.components.ProdutoComponent
import com.nadir.apiprodutos.entities.Produto
import com.nadir.apiprodutos.exceptions.EstoqueNaoZeradoException
import com.nadir.apiprodutos.exceptions.NotFoundException
import com.nadir.apiprodutos.repositories.ProdutoRepository
import com.nadir.apiprodutos.services.ProdutoService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.*
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.math.BigDecimal
import java.util.*


@Testcontainers
@SpringBootTest
@ExtendWith(MockKExtension::class)
class ProdutoServiceTest {

    companion object {
//        @Container
//        val container = PostgreSQLContainer<Nothing>("postgres:12").apply {
//            withDatabaseName("postgres")
//            withUsername("postgres")
//            withPassword("docker")
//        }
        @Container
        val container = MySQLContainer<Nothing>("mysql").apply {
            withDatabaseName("apiprodutos")
            withUsername("root")
            withPassword("admin")
        }

        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url", container::getJdbcUrl);
            registry.add("spring.datasource.password", container::getPassword);
            registry.add("spring.datasource.username", container::getUsername);
        }
    }


    @InjectMockKs
    private lateinit var produtoService: ProdutoService

    @MockK
    private lateinit var produtoRepository: ProdutoRepository

    private lateinit var produto: Produto
    private lateinit var produtoList: List<Produto>

    @BeforeEach
    fun setup() {
        produtoList = listOf(
        ProdutoComponent.createActiveProdutoEntity(),
        ProdutoComponent.createActiveProdutoEntity(),
        ProdutoComponent.createActiveProdutoEntity(),
        ProdutoComponent.createActiveProdutoEntity(),
        ProdutoComponent.createActiveProdutoEntity(),
        ProdutoComponent.createActiveProdutoEntity()
        )
    }

    @Test
    fun `quando solicita allProdutos o repositorio deve retornar uma lista populada`() {
        every { produtoRepository.findAll() } returns produtoList

        val products = produtoService.findAll()

        verify(exactly = 1) { produtoRepository.findAll() }
        assertEquals(produtoList, products)
        assertEquals(produtoList.size, products.size)
    }

    @Test
    fun `quando solicitado salva um produto no repository`() {
        produto = ProdutoComponent.createActiveProdutoEntity()
        every { produtoRepository.save(produto) } returns produto

        val product = produtoService.save(produto)

        verify(exactly = 1) { produtoRepository.save(any())}
        assertEquals(product, produto)
    }

    @Test
    fun `quando desativa um produto este é desativado`() {
        produto = ProdutoComponent.createActiveProdutoEntity()
        produto.quantidade = BigDecimal.ZERO
        produto.quantidadeReservadaCarrinho = BigDecimal.ZERO
        val statusAnterior: Boolean = produto.isActive
        val statusPosterior: Boolean
        val id: Long = produto.id!!


        //`when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
        //`when`(produtoRepository.save(produto)).thenReturn(produto)
        every { produtoRepository.findById(id) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto

        statusPosterior = produtoService.disable(id).isActive

        //verify(produtoRepository, atLeastOnce()).findById(id)
        verify(exactly = 1) { produtoRepository.save(any())}
        verify(exactly = 1) { produtoRepository.findById(id)}

        //verify(produtoRepository, atLeastOnce()).save(produto)

        assertEquals(true, statusAnterior)
        assertEquals(false, statusPosterior)
        assertNotEquals(statusPosterior, statusAnterior)
    }

    @Test
    fun `quando desativa um produto com estoque não zerado espera mensagem de excecao`() {
        produto = ProdutoComponent.createActiveProdutoEntity()
        produto.quantidade = BigDecimal.TEN
        produto.quantidadeReservadaCarrinho = BigDecimal.ONE
        val id: Long = produto.id!!
        //`when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
        every { produtoRepository.findById(id) } returns Optional.of(produto)
        try{
            produtoService.disable(id).isActive
            verify(produtoRepository, atLeastOnce()).findById(id)
            verify(produtoRepository, atLeastOnce()).save(produto)
        } catch(ex: EstoqueNaoZeradoException) {
            assertEquals("O estoque precisa estar zerado para desativar o produto".format(id), ex.message)
        }
    }


    @Test
    fun `quando se tenta desativar com id inexistente lanca excecao NotFound`() {
        produto = ProdutoComponent.createInactiveProdutoEntity()

        val id = 10L
        //`when`(produtoRepository.findById(id)).thenReturn(Optional.empty())
        every { produtoRepository.findById(id) } returns Optional.empty()
        every { produtoRepository.save(produto) } returns produto
        assertThrows<NotFoundException> { produtoService.disable(id) }
    }

    @Test
    fun `quando se tenta desativar com id inexistente lanca excecao`() {
        produto = ProdutoComponent.createInactiveProdutoEntity()

        val id: Long = produto.id!!
        //`when`(produtoRepository.findById(id)).thenReturn(Optional.empty())
        every { produtoRepository.findById(id) } returns Optional.of(produto)
        every { produtoRepository.save(produto) } returns produto
        try{
            produtoService.disable(id)
        } catch (ex: NotFoundException) {
            assertEquals("Produto com id %s não localizado.".format(id), ex.message)
        }
    }

    @Test
    fun `quando ativa um produto este é ativado`() {
        produto = ProdutoComponent.createInactiveProdutoEntity()
        val statusAnterior: Boolean = produto.isActive
        val statusPosterior: Boolean
        val id: Long = produto.id!!

        //`when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
        every { produtoRepository.findById(id) } returns Optional.of(produto)
        //`when`(produtoRepository.save(produto)).thenReturn(produto)
        every { produtoRepository.save(produto) } returns produto
        statusPosterior = produtoService.enable(id).isActive

        //verify(produtoRepository, atLeastOnce()).findById(id)
        verify(exactly = 1) { produtoRepository.findById(any()) }
        verify(exactly = 1) { produtoRepository.save(produto) }
        //verify(produtoRepository, atLeastOnce()).save(produto)

        assertEquals(false, statusAnterior)
        assertEquals(true, statusPosterior)
        assertNotEquals(statusPosterior, statusAnterior)
    }

    @Test
    fun `quando busca um produto por id este é encontrado`() {
        produto = ProdutoComponent.createActiveProdutoEntity()
        val id: Long = produto.id!!
        //`when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
        every{ produtoRepository.findById(id)} returns Optional.of(produto)
        val produtoARetornar = produtoService.findById(id)
        //verify(produtoRepository, only()).findById(id)
        verify(exactly = 1) { produtoRepository.findById(any()) }
        assertEquals(produto, produtoARetornar)
    }
}