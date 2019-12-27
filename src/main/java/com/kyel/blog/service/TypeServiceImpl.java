package com.kyel.blog.service;

import com.kyel.blog.NotFoundException;
import com.kyel.blog.dao.TypeRepository;
import com.kyel.blog.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;

    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        Optional<Type> type = typeRepository.findById(id);
        if (type.isPresent())
            return type.get();
        else
            return null;
    }

    @Override
    public Type getTypeByName(String name) {
        Optional<Type> type = typeRepository.findByName(name);
        if (type.isPresent())
            return type.get();
        else
            return null;
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "blogs.size");
        Pageable pageable = PageRequest.of(0, size, sort);
        return typeRepository.findTop(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Optional<Type> targetType = typeRepository.findById(id);
        if (!targetType.isPresent())
            throw new NotFoundException("此类型不存在");
        BeanUtils.copyProperties(type, targetType.get());
        return typeRepository.save(targetType.get());
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.deleteById(id);
    }
}
