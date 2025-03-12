# Засіб односторонньої автентифікації користувачів з використанням 3DES та міток часу

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

Проект реалізує механізм односторонньої автентифікації на основі алгоритму 3DES та міток часу для захисту від повторних атак. Використовує клієнт-серверну архітектуру.

## Зміст
- [Особливості](#особливості)
- [Технології](#технології)
- [Встановлення](#встановлення)
- Автор - https://github.com/santabelsDV

## Особливості
- **3DES-шифрування**: Використання трійного DES з ключем 168 біт.
- **Захист від replay-атак**: Мітки часу обмежують час дії запитів (1 секунда).
- **Клієнт-серверна модель**: Взаємодія через сокети на порту 8080.
- **Генерація ключів**: Можливість використання згенерованих або користувацьких ключів (хеш SHA-1).

## Технології
- **Мова**: Java 17+
- **Бібліотеки**: JCA (Java Cryptography Architecture)
- **Середовище**: IntelliJ IDEA (рекомендоване)
- **Збірка**: Maven/Gradle (опційно)

## Встановлення
1. Клонуйте репозиторій:
   ```bash
   git clone https://github.com/your-username/one-way-auth-3des.git