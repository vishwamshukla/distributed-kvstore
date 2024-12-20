package com.distributed.kvstore.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kv")
public class KeyValueController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // Create or Update the Key-Value Pair
    @PostMapping("/{key}")
    public ResponseEntity<String> put(@PathVariable String key, @RequestBody String value){
        redisTemplate.opsForValue().set(key, value);
        return ResponseEntity.ok("Key-Value pair saved");
    }

    //Read a value by Key
    @GetMapping("/{key}")
    public ResponseEntity<String> get(@PathVariable String key){
        String value = redisTemplate.opsForValue().get(key);
        return value !=null ? ResponseEntity.ok(value) : ResponseEntity.notFound().build();
    }

    //Delete a Key-Value Pair
    @DeleteMapping("/{key}")
    public ResponseEntity<String> delete(@PathVariable String key){
        redisTemplate.delete(key);
        return ResponseEntity.ok("Key deleted!");
    }
}
