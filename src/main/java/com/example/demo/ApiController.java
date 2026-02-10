package com.example.demo;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;


import java.util.*;

@RestController
public class ApiController {

 private final String EMAIL = "ishan0694.be23@chitkara.edu.in";

 // HEALTH API
 @GetMapping("/health")
 public Map<String, Object> health() {

  Map<String, Object> res = new HashMap<>();
  res.put("is_success", true);
  res.put("official_email", EMAIL);

  return res;
 }

 // BFHL API
 @PostMapping("/bfhl")
 public ResponseEntity<?> bfhl(@RequestBody Map<String, Object> body) {

  Map<String, Object> res = new HashMap<>();

  try {

   if (body.containsKey("fibonacci")) {
    int n = (int) body.get("fibonacci");
    res.put("data", fibonacci(n));
   }

   else if (body.containsKey("prime")) {
    List<Integer> list = (List<Integer>) body.get("prime");
    res.put("data", getPrime(list));
   }

   else if (body.containsKey("lcm")) {
    List<Integer> list = (List<Integer>) body.get("lcm");
    res.put("data", lcm(list));
   }

   else if (body.containsKey("hcf")) {
    List<Integer> list = (List<Integer>) body.get("hcf");
    res.put("data", hcf(list));
   }
   else if (body.containsKey("AI")) {
    String q = (String) body.get("AI");
    res.put("data", askAI(q));
}


   else {
    throw new Exception("Invalid input");
   }

   res.put("is_success", true);
   res.put("official_email", EMAIL);

   return ResponseEntity.ok(res);

  } catch (Exception e) {

   res.put("is_success", false);
   res.put("error", e.getMessage());

   return ResponseEntity.badRequest().body(res);
  }
 }

 // LOGIC FUNCTIONS

 private List<Integer> fibonacci(int n) {

  List<Integer> list = new ArrayList<>();
  int a = 0, b = 1;

  for (int i = 0; i < n; i++) {
   list.add(a);
   int c = a + b;
   a = b;
   b = c;
  }

  return list;
 }

 private List<Integer> getPrime(List<Integer> nums) {

  List<Integer> primes = new ArrayList<>();

  for (int n : nums)
   if (isPrime(n))
    primes.add(n);

  return primes;
 }

 private boolean isPrime(int n) {

  if (n < 2) return false;

  for (int i = 2; i * i <= n; i++)
   if (n % i == 0)
    return false;

  return true;
 }

 private int hcf(List<Integer> nums) {

  int res = nums.get(0);

  for (int n : nums)
   res = gcd(res, n);

  return res;
 }

 private int gcd(int a, int b) {
  return b == 0 ? a : gcd(b, a % b);
 }

 private int lcm(List<Integer> nums) {

  int res = nums.get(0);

  for (int n : nums)
   res = res * n / gcd(res, n);

  return res;
 }
 private String askAI(String question) {

    String apiKey = "AIzaSyB0QOGyOFVtvFSuNVU4tjb_CEZ1FouFZvY";

    String url =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
        + apiKey;

    RestTemplate restTemplate = new RestTemplate();

    String body = """
        {
          "contents": [{
            "parts":[{"text":"Answer in ONE WORD only: %s"}]
          }]
        }
        """.formatted(question);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(body, headers);

    try {

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, entity, Map.class);

        Map resBody = response.getBody();

        // Extract only answer text
        Map candidate = (Map) ((List) resBody.get("candidates")).get(0);
        Map content = (Map) candidate.get("content");
        Map part = (Map) ((List) content.get("parts")).get(0);

        return part.get("text").toString().trim();

    } catch (Exception e) {
        e.printStackTrace();
        return "AI_ERROR";
    }
}


}
