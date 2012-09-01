# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)


# WARNING: This command will destroy all records in the database

require 'json'

SEED_DIR = 'db/seeds'


# AR models are lazy-loaded, so load them and clean out:
User.delete_all
Incident.delete_all
IncidentUpdate.delete_all

class Incident
  attr_accessible :created_at, :updated_at
end

class IncidentUpdate
  attr_accessible :created_at, :updated_at
end

User.create(email: "foo@example.com")
User.create(email: "bar@example.com")
users = User.all

json_files = Dir.entries(SEED_DIR).keep_if {|x| x =~ /\.json\z/ }

json_files.each do |json_file|

  file_path = SEED_DIR + "/" + json_file
  incident = JSON.load(File.read(file_path))

  # Massage data so it fits our ActiveRecord models
  incident.delete_if { |k,v| k =~ /\Astatus_(dev|prod)|id|href\z/ }
  incident['updates'].each do |update|
    update['content'] = update['contents']
    regex = /\Astatus_(dev|prod)|(incident_)?id|contents\z/
    update.delete_if { |k,v| k =~ regex }
  end

  ar_updates = []
  updates = incident.delete('updates').reverse

  updates.each do |update|
    iu = IncidentUpdate.new(update)
    iu.creator = users.sample
    iu.updater = users.sample
    ar_updates << iu
  end

  ar_incident = Incident.new(incident)
  ar_incident.creator = users.sample
  ar_incident.updater = users.sample
  ar_incident.incident_updates = ar_updates
  ar_incident.save
end