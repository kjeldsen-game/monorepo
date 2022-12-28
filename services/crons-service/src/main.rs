use dotenv::dotenv;

fn main() {
    dotenv().ok(); // Load environment variables
    crons_service::run();
}

