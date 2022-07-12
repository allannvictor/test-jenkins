package com.nadir.apiprodutos.service

import com.nadir.apiprodutos.components.ProdutoComponent
import com.nadir.apiprodutos.entities.Produto
import com.nadir.apiprodutos.repositories.ProdutoRepository
import com.nadir.apiprodutos.services.ProdutoService
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration


@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration
@ExtendWith(MockKExtension::class)
class ProdutoServiceTest {

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

//    @Test
//    fun `quando desativa um produto este é desativado`() {
//        produto = ProdutoComponent.createActiveProdutoEntity()
//        produto.quantidade = BigDecimal.ZERO
//        produto.quantidadeReservadaCarrinho = BigDecimal.ZERO
//        val statusAnterior: Boolean = produto.isActive
//        val statusPosterior: Boolean
//        val id: Long = produto.id!!
//
//        `when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
//        `when`(produtoRepository.save(produto)).thenReturn(produto)
//        statusPosterior = produtoService.disable(id).isActive
//
//        verify(produtoRepository, atLeastOnce()).findById(id)
//        verify(produtoRepository, atLeastOnce()).save(produto)
//
//        assertEquals(true, statusAnterior)
//        assertEquals(false, statusPosterior)
//        assertNotEquals(statusPosterior, statusAnterior)
//    }
//
//    @Test
//    fun `quando desativa um produto com estoque não zerado espera mensagem de excecao`() {
//        produto = ProdutoComponent.createActiveProdutoEntity()
//        produto.quantidade = BigDecimal.TEN
//        produto.quantidadeReservadaCarrinho = BigDecimal.ONE
//        val id: Long = produto.id!!
//        `when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
//        try{
//            produtoService.disable(id).isActive
//            verify(produtoRepository, atLeastOnce()).findById(id)
//            verify(produtoRepository, atLeastOnce()).save(produto)
//        } catch(ex: EstoqueNaoZeradoException) {
//            assertEquals("O estoque precisa estar zerado para desativar o produto".format(id), ex.message)
//        }
//    }
//
//
//    @Test
//    fun `quando se tenta desativar com id inexistente lanca excecao NotFound`() {
//        val id: Long = 10L
//        `when`(produtoRepository.findById(id)).thenReturn(Optional.empty())
//        assertThrows<NotFoundException> { produtoService.disable(id) }
//    }
//
//    @Test
//    fun `quando se tenta desativar com id inexistente lanca excecao`() {
//        val id: Long = 10L
//        `when`(produtoRepository.findById(id)).thenReturn(Optional.empty())
//        try{
//            produtoService.disable(id)
//        } catch (ex: NotFoundException) {
//            assertEquals("Produto com id %s não localizado.".format(id), ex.message)
//        }
//    }
//
//    @Test
//    fun `quando ativa um produto este é ativado`() {
//        produto = ProdutoComponent.createInactiveProdutoEntity()
//        val statusAnterior: Boolean = produto.isActive
//        val statusPosterior: Boolean
//        val id: Long = produto.id!!
//
//        `when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
//        `when`(produtoRepository.save(produto)).thenReturn(produto)
//        statusPosterior = produtoService.enable(id).isActive
//
//        verify(produtoRepository, atLeastOnce()).findById(id)
//        verify(produtoRepository, atLeastOnce()).save(produto)
//
//        assertEquals(false, statusAnterior)
//        assertEquals(true, statusPosterior)
//        assertNotEquals(statusPosterior, statusAnterior)
//    }
//
//    @Test
//    fun `quando busca um produto por id este é encontrado`() {
//        produto = ProdutoComponent.createActiveProdutoEntity()
//        val id: Long = produto.id!!
//        `when`(produtoRepository.findById(id)).thenReturn(Optional.of(produto))
//        var produtoARetornar = produtoService.findById(id)
//        verify(produtoRepository, only()).findById(id)
//        assertEquals(produto, produtoARetornar)
//    }
}