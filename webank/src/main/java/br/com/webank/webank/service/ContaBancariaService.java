package br.com.webank.webank.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.webank.webank.dto.contaBancaria.ContaBancariaRequestDTO;
import br.com.webank.webank.dto.contaBancaria.ContaBancariaResponseDTO;
import br.com.webank.webank.model.ContaBancaria;
import br.com.webank.webank.repository.ContaBancariaRepository;

@Service
public class ContaBancariaService {
    
    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<ContaBancariaResponseDTO> obterTodos(){
        return contaBancariaRepository.findAll()
                .stream()
                .map(contaBancaria -> modelMapper.map(contaBancaria, ContaBancariaResponseDTO.class))
                .collect(Collectors.toList());
    }

    public ContaBancariaResponseDTO obterPorId(long id){
        Optional<ContaBancaria> optContaBancaria = contaBancariaRepository.findById(id);

        if(optContaBancaria.isEmpty()){
            throw new RuntimeException("Nenhum registro encontrado para o ID: " + id);
        }

        return modelMapper.map(optContaBancaria.get(), ContaBancariaResponseDTO.class);
    }

    // O save serve tanto para adicionar quanto para atualizar.
    // se tiver id, ele atualiza, s enão tiver id ele adiciona.
    public ContaBancariaResponseDTO adicionar(ContaBancariaRequestDTO contaBancariaRequest){
        ContaBancaria contaBancaria = modelMapper.map(contaBancariaRequest, ContaBancaria.class);
        contaBancaria.setId(0);
        contaBancaria = contaBancariaRepository.save(contaBancaria);
        return modelMapper.map(contaBancaria, ContaBancariaResponseDTO.class);
    }

    public ContaBancariaResponseDTO atualizar(long id, ContaBancariaRequestDTO contaBancariaRequest){
        // Se não lançar exception é porque o cara existe no banco.
        obterPorId(id);
         ContaBancaria contaBancaria = modelMapper.map(contaBancariaRequest, ContaBancaria.class);
        contaBancaria.setId(id);
        contaBancaria = contaBancariaRepository.save(contaBancaria);
        return modelMapper.map(contaBancaria, ContaBancariaResponseDTO.class);
    }

    public void deletar(Long id){
        obterPorId(id);

        contaBancariaRepository.deleteById(id);
    }

}
