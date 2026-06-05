
CREATE TABLE users(
    id UUID primary key,
    name VARCHAR(255),
    email VARCHAR(255),
    password VARCHAR(255),
    created_date TIMESTAMP default CURRENT_TIMESTAMP,
    updated_date TIMESTAMP default  CURRENT_TIMESTAMP
)

CREATE TABLE user_roles(
   user_id UUID NOT NULL,
   role VARCHAR(50) NOT NULL,

   CONSTRAINT fk_user_roles_user_id
       FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
   CONSTRAINT pk_user_roles
       PRIMARY KEY (user_id, role)
)

