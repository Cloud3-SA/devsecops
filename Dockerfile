# Utilize uma imagem base do Node.js (Alpine para manter pequena)
FROM node:14-alpine

# Defina a pasta de trabalho
WORKDIR /usr/src/app

# Copie o arquivo package.json e package-lock.json (se disponível)
COPY package*.json ./

# Instale as dependências do projeto
RUN npm ci

# Copie os arquivos do projeto para a pasta de trabalho
COPY . .

# Exponha a porta em que sua aplicação irá rodar, se aplicável
EXPOSE 3000

# Inicie a aplicação com o comando específico
CMD ["npm", "start"]
