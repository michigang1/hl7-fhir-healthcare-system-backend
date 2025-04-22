INSERT INTO roles(name) VALUES
                            ('ROLE_PATIENT'),
                            ('ROLE_DOCTOR'),
                            ('ROLE_ADMIN'),
                            ('ROLE_SOCIAL_WORKER'),
                            ('ROLE_NURSE'),
                            ('ROLE_PHARMACIST'),
                            ('ROLE_RECEPTIONIST')
ON CONFLICT (name) DO NOTHING;
ddDdd