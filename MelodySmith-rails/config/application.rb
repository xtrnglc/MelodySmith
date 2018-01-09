require_relative 'boot'

require 'rails/all'
#require 'aws-sdk'


# Require the gems listed in Gemfile, including any gems
# you've limited to :test, :development, or :production.
Bundler.require(*Rails.groups)

module MelodySmith
  class Application < Rails::Application
    # Initialize configuration defaults for originally generated Rails version.
    config.load_defaults 5.1

    # Settings in config/environments/* take precedence over those specified here.
    # Application configuration should go into files in config/initializers
    # -- all .rb files in that directory are automatically loaded.
    # Aws.config[:region] = 'us-east-1'
    # Aws.config[:credentials] = Aws::Credentials.new('AKIAJUPHP6Y6TGV7RBEQ', 'EIH7RIs1vXXANFpkURvqNVJx9xdaYyuFc6tV7QN4')
    # BUCKET='melodysmith_files'
  end
end

