# https://hub.docker.com/_/rust

FROM rust:1.61

WORKDIR /usr/src/myapp
COPY . .

RUN cargo install --path .

CMD ["myapp"]
