// 导入KEY以便进行PSA操作
void import_a_key(const uint8_t *key, size_t key_len)
{
    psa_status_t status;
    psa_key_attributes_t attributes = PSA_KEY_ATTRIBUTES_INIT;
    psa_key_id_t key_id;

    printf("Import an AES key...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Set key attributes */
    psa_set_key_usage_flags(&attributes, 0);
    psa_set_key_algorithm(&attributes, 0);
    psa_set_key_type(&attributes, PSA_KEY_TYPE_AES);
    psa_set_key_bits(&attributes, 128);

    /* Import the key */
    status = psa_import_key(&attributes, key, key_len, &key_id);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to import key\n");
        return;
    }
    printf("Imported a key\n");

    /* Free the attributes */
    psa_reset_key_attributes(&attributes);

    /* Destroy the key */
    psa_destroy_key(key_id);

    mbedtls_psa_crypto_free();
}

// RSA签名
void sign_a_message_using_rsa(const uint8_t *key, size_t key_len)
{
    psa_status_t status;
    psa_key_attributes_t attributes = PSA_KEY_ATTRIBUTES_INIT;
    uint8_t hash[32] = {0x50, 0xd8, 0x58, 0xe0, 0x98, 0x5e, 0xcc, 0x7f,
                        0x60, 0x41, 0x8a, 0xaf, 0x0c, 0xc5, 0xab, 0x58,
                        0x7f, 0x42, 0xc2, 0x57, 0x0a, 0x88, 0x40, 0x95,
                        0xa9, 0xe8, 0xcc, 0xac, 0xd0, 0xf6, 0x54, 0x5c};
    uint8_t signature[PSA_SIGNATURE_MAX_SIZE] = {0};
    size_t signature_length;
    psa_key_id_t key_id;

    printf("Sign a message...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Set key attributes */
    psa_set_key_usage_flags(&attributes, PSA_KEY_USAGE_SIGN_HASH);
    psa_set_key_algorithm(&attributes, PSA_ALG_RSA_PKCS1V15_SIGN_RAW);
    psa_set_key_type(&attributes, PSA_KEY_TYPE_RSA_KEY_PAIR);
    psa_set_key_bits(&attributes, 1024);

    /* Import the key */
    status = psa_import_key(&attributes, key, key_len, &key_id);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to import key\n");
        return;
    }

    /* Sign message using the key */
    status = psa_sign_hash(key_id, PSA_ALG_RSA_PKCS1V15_SIGN_RAW,
                           hash, sizeof(hash),
                           signature, sizeof(signature),
                           &signature_length);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to sign\n");
        return;
    }

    printf("Signed a message\n");

    /* Free the attributes */
    psa_reset_key_attributes(&attributes);

    /* Destroy the key */
    psa_destroy_key(key_id);

    mbedtls_psa_crypto_free();
}

// 对称加密
void encrypt_with_symmetric_ciphers(const uint8_t *key, size_t key_len)
{
    enum
    {
        block_size = PSA_BLOCK_CIPHER_BLOCK_LENGTH(PSA_KEY_TYPE_AES),
    };
    psa_status_t status;
    psa_key_attributes_t attributes = PSA_KEY_ATTRIBUTES_INIT;
    psa_algorithm_t alg = PSA_ALG_CBC_NO_PADDING;
    uint8_t plaintext[block_size] = SOME_PLAINTEXT;
    uint8_t iv[block_size];
    size_t iv_len;
    uint8_t output[block_size];
    size_t output_len;
    psa_key_id_t key_id;
    psa_cipher_operation_t operation = PSA_CIPHER_OPERATION_INIT;

    printf("Encrypt with cipher...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Import a key */
    psa_set_key_usage_flags(&attributes, PSA_KEY_USAGE_ENCRYPT);
    psa_set_key_algorithm(&attributes, alg);
    psa_set_key_type(&attributes, PSA_KEY_TYPE_AES);
    psa_set_key_bits(&attributes, 128);
    status = psa_import_key(&attributes, key, key_len, &key_id);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to import a key\n");
        return;
    }
    psa_reset_key_attributes(&attributes);

    /* Encrypt the plaintext */
    status = psa_cipher_encrypt_setup(&operation, key_id, alg);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to begin cipher operation\n");
        return;
    }
    status = psa_cipher_generate_iv(&operation, iv, sizeof(iv), &iv_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to generate IV\n");
        return;
    }
    status = psa_cipher_update(&operation, plaintext, sizeof(plaintext),
                               output, sizeof(output), &output_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to update cipher operation\n");
        return;
    }
    status = psa_cipher_finish(&operation, output + output_len,
                               sizeof(output) - output_len, &output_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to finish cipher operation\n");
        return;
    }
    printf("Encrypted plaintext\n");

    /* Clean up cipher operation context */
    psa_cipher_abort(&operation);

    /* Destroy the key */
    psa_destroy_key(key_id);

    mbedtls_psa_crypto_free();
}

// 对称解密
void decrypt_with_symmetric_ciphers(const uint8_t *key, size_t key_len)
{
    enum
    {
        block_size = PSA_BLOCK_CIPHER_BLOCK_LENGTH(PSA_KEY_TYPE_AES),
    };
    psa_status_t status;
    psa_key_attributes_t attributes = PSA_KEY_ATTRIBUTES_INIT;
    psa_algorithm_t alg = PSA_ALG_CBC_NO_PADDING;
    psa_cipher_operation_t operation = PSA_CIPHER_OPERATION_INIT;
    uint8_t ciphertext[block_size] = SOME_CIPHERTEXT;
    uint8_t iv[block_size] = ENCRYPTED_WITH_IV;
    uint8_t output[block_size];
    size_t output_len;
    psa_key_id_t key_id;

    printf("Decrypt with cipher...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Import a key */
    psa_set_key_usage_flags(&attributes, PSA_KEY_USAGE_DECRYPT);
    psa_set_key_algorithm(&attributes, alg);
    psa_set_key_type(&attributes, PSA_KEY_TYPE_AES);
    psa_set_key_bits(&attributes, 128);
    status = psa_import_key(&attributes, key, key_len, &key_id);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to import a key\n");
        return;
    }
    psa_reset_key_attributes(&attributes);

    /* Decrypt the ciphertext */
    status = psa_cipher_decrypt_setup(&operation, key_id, alg);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to begin cipher operation\n");
        return;
    }
    status = psa_cipher_set_iv(&operation, iv, sizeof(iv));
    if (status != PSA_SUCCESS)
    {
        printf("Failed to set IV\n");
        return;
    }
    status = psa_cipher_update(&operation, ciphertext, sizeof(ciphertext),
                               output, sizeof(output), &output_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to update cipher operation\n");
        return;
    }
    status = psa_cipher_finish(&operation, output + output_len,
                               sizeof(output) - output_len, &output_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to finish cipher operation\n");
        return;
    }
    printf("Decrypted ciphertext\n");

    /* Clean up cipher operation context */
    psa_cipher_abort(&operation);

    /* Destroy the key */
    psa_destroy_key(key_id);

    mbedtls_psa_crypto_free();
}

// SHA256计算
void sha256_calc()
{
    psa_status_t status;
    psa_algorithm_t alg = PSA_ALG_SHA_256;
    psa_hash_operation_t operation = PSA_HASH_OPERATION_INIT;
    unsigned char input[] = {'a', 'b', 'c'};
    unsigned char actual_hash[PSA_HASH_MAX_SIZE];
    size_t actual_hash_len;

    printf("Hash a message...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Compute hash of message  */
    status = psa_hash_setup(&operation, alg);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to begin hash operation\n");
        return;
    }
    status = psa_hash_update(&operation, input, sizeof(input));
    if (status != PSA_SUCCESS)
    {
        printf("Failed to update hash operation\n");
        return;
    }
    status = psa_hash_finish(&operation, actual_hash, sizeof(actual_hash),
                             &actual_hash_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to finish hash operation\n");
        return;
    }

    printf("Hashed a message\n");

    /* Clean up hash operation context */
    psa_hash_abort(&operation);

    mbedtls_psa_crypto_free();
}

// SHA256校验
void sha256_verify()
{
    psa_status_t status;
    psa_algorithm_t alg = PSA_ALG_SHA_256;
    psa_hash_operation_t operation = PSA_HASH_OPERATION_INIT;
    unsigned char input[] = {'a', 'b', 'c'};
    unsigned char expected_hash[] = {
        0xba, 0x78, 0x16, 0xbf, 0x8f, 0x01, 0xcf, 0xea, 0x41, 0x41, 0x40, 0xde,
        0x5d, 0xae, 0x22, 0x23, 0xb0, 0x03, 0x61, 0xa3, 0x96, 0x17, 0x7a, 0x9c,
        0xb4, 0x10, 0xff, 0x61, 0xf2, 0x00, 0x15, 0xad};
    size_t expected_hash_len = PSA_HASH_LENGTH(alg);

    printf("Verify a hash...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Verify message hash */
    status = psa_hash_setup(&operation, alg);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to begin hash operation\n");
        return;
    }
    status = psa_hash_update(&operation, input, sizeof(input));
    if (status != PSA_SUCCESS)
    {
        printf("Failed to update hash operation\n");
        return;
    }
    status = psa_hash_verify(&operation, expected_hash, expected_hash_len);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to verify hash\n");
        return;
    }

    printf("Verified a hash\n");

    /* Clean up hash operation context */
    psa_hash_abort(&operation);
    mbedtls_psa_crypto_free();
}

// 生成ECDSA密钥对
void exportECDSA()
{
    enum
    {
        key_bits = 256,
    };
    psa_status_t status;
    size_t exported_length = 0;
    static uint8_t exported[PSA_KEY_EXPORT_ECC_PUBLIC_KEY_MAX_SIZE(key_bits)];
    psa_key_attributes_t attributes = PSA_KEY_ATTRIBUTES_INIT;
    psa_key_id_t key_id;

    printf("Generate a key pair...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    /* Generate a key */
    psa_set_key_usage_flags(&attributes, PSA_KEY_USAGE_SIGN_HASH);
    psa_set_key_algorithm(&attributes,
                          PSA_ALG_DETERMINISTIC_ECDSA(PSA_ALG_SHA_256));
    psa_set_key_type(&attributes,
                     PSA_KEY_TYPE_ECC_KEY_PAIR(PSA_ECC_FAMILY_SECP_R1));
    psa_set_key_bits(&attributes, key_bits);
    status = psa_generate_key(&attributes, &key_id);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to generate key\n");
        return;
    }
    psa_reset_key_attributes(&attributes);

    status = psa_export_public_key(key_id, exported, sizeof(exported),
                                   &exported_length);
    if (status != PSA_SUCCESS)
    {
        printf("Failed to export public key %ld\n", status);
        return;
    }

    printf("Exported a public key\n");

    /* Destroy the key */
    psa_destroy_key(key_id);
    mbedtls_psa_crypto_free();
}

// 生成随机数
void generateRandomNumber()
{
    psa_status_t status;
    uint8_t random[10] = {0};

    printf("Generate random...\t");
    fflush(stdout);

    /* Initialize PSA Crypto */
    status = psa_crypto_init();
    if (status != PSA_SUCCESS)
    {
        printf("Failed to initialize PSA Crypto\n");
        return;
    }

    status = psa_generate_random(random, sizeof(random));
    if (status != PSA_SUCCESS)
    {
        printf("Failed to generate a random value\n");
        return;
    }

    printf("Generated random data\n");

    /* Clean up */
    mbedtls_psa_crypto_free();
}
