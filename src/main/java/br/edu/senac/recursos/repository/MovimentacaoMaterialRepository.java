package br.edu.senac.recursos.repository;

import br.edu.senac.recursos.domain.MovimentacaoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimentacaoMaterialRepository extends JpaRepository<MovimentacaoMaterial, Long> {

    List<MovimentacaoMaterial> findByMaterialIdOrderByInstanteAsc(Long materialId);

    @Query("select coalesce(sum(m.quantidade),0) from MovimentacaoMaterial m " +
           "where m.material.id = :id and m.tipo = :tipo")
    long somarPorTipo(@Param("id") Long materialId, @Param("tipo") String tipo);
}
